package com.botscrew.framework.flow.container;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.reflections.Reflections;

import com.botscrew.framework.flow.annotation.Text;
import com.botscrew.framework.flow.annotation.TextProcessor;
import com.botscrew.framework.flow.exception.DuplicatedActionException;
import com.botscrew.framework.flow.exception.ProcessorInnerException;
import com.botscrew.framework.flow.exception.WrongMethodSignatureException;
import com.botscrew.framework.flow.model.ArgumentType;
import com.botscrew.framework.flow.model.ChatUser;
import com.botscrew.framework.flow.model.InstanceMethod;
import com.botscrew.framework.flow.util.ParametersUtils;
import com.botscrew.framework.flow.util.TypeChecker;

public class TextContainer<U extends ChatUser> extends AbstractContainer {

	private final Map<String, InstanceMethod> textActionsMap;

	public TextContainer(String packageName) {
		super(packageName);
		textActionsMap = new ConcurrentHashMap<>();
	}

	public TextContainer(String packageName, String spliterator) {
		super(packageName, spliterator);
		textActionsMap = new ConcurrentHashMap<>();
	}

	@PostConstruct
	private void init() {
		Reflections reflections = new Reflections(packageName);
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(TextProcessor.class);

		annotated.forEach(clazz -> {
			Stream.of(clazz.getMethods()).filter(m -> m.isAnnotationPresent(Text.class)).forEach(m -> {
				List<ArgumentType> arguments = getArgumentTypes(m);
				InstanceMethod instanceMethod = new InstanceMethod(context.getBean(clazz), m, arguments);
				Text text = m.getAnnotation(Text.class);

				if (text.states().length == 0) {
					addAction(ALL_STATES, instanceMethod);
				} else {
					for (String state : text.states()) {
						addAction(state, instanceMethod);
					}
				}
			});
		});
	}

	public void processText(String text, U user) {

		InstanceMethod instanceMethod = findInstanceMethod(user);

		try {
			instanceMethod.getMethod().invoke(instanceMethod.getInstance(),
					getArguments(instanceMethod.getArgumentTypes(), text, user));
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new ProcessorInnerException(e.getCause());
		}

	}

	private InstanceMethod findInstanceMethod(ChatUser user) {
		String stateValue = ParametersUtils.getValueWithoutParams(user.getState(), spliterator);
		InstanceMethod instanceMethod = textActionsMap.get(stateValue);

		if (instanceMethod == null) {
			instanceMethod = textActionsMap.get(ALL_STATES);
			if (instanceMethod == null) {
				throw new IllegalArgumentException(
						"No method with annotation @Text which meets given parameters,  state:" + user.getState());
			}
		}

		return instanceMethod;
	}

	private void addAction(String state, InstanceMethod instanceMethod) {
		if (textActionsMap.containsKey(state)) {
			throw new DuplicatedActionException("Duplication of text processing action: state = " + state);
		}
		textActionsMap.put(state, instanceMethod);
	}

	@Override
	protected ArgumentType getArgumentType(Class<?> type, Annotation[] annotations) {
		if (type.equals(String.class)) {
			return ArgumentType.TEXT;
		}
		if (TypeChecker.isInterfaceImplementing(type, ChatUser.class)) {
			return ArgumentType.USER;
		}
		if (TypeChecker.isInterfaceImplementing(type, Map.class)) {
			return ArgumentType.STATE_PARAMETERS;
		}
		throw new WrongMethodSignatureException(
				"Methods can only contain next parameters: " + "String value of user's message, ChatUser "
						+ "and Map<String,String> state parameters. All of these parameters are optional");
	}

	private Object[] getArguments(List<ArgumentType> types, String text, ChatUser user) {
		final Object[] result = new Object[types.size()];
		IntStream.range(0, types.size()).forEach(index -> {
			switch (types.get(index)) {
			case USER:
				result[index] = user;
				break;
			case TEXT:
				result[index] = text;
				break;
			case STATE_PARAMETERS:
				result[index] = ParametersUtils.getParameters(user.getState(), spliterator);
				break;
			default:
				throw new WrongMethodSignatureException("Wrong parameters");
			}

		});

		return result;

	}

}
