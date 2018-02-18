package com.botscrew.framework.flow.container;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.botscrew.framework.flow.annotation.Text;
import com.botscrew.framework.flow.model.ChatUser;

import com.botscrew.framework.flow.exception.DuplicatedActionException;
import com.botscrew.framework.flow.exception.ProcessorInnerException;
import com.botscrew.framework.flow.exception.WrongMethodSignatureException;
import com.botscrew.framework.flow.model.ArgumentType;
import com.botscrew.framework.flow.model.InstanceMethod;
import com.botscrew.framework.flow.util.ParametersUtils;
import com.botscrew.framework.flow.util.TypeChecker;

public class TextContainer extends AbstractContainer {

	private final Map<String, InstanceMethod> textActionsMap;

	public TextContainer() {
		super();
		textActionsMap = new ConcurrentHashMap<>();
	}

	public TextContainer(String spliterator) {
		super(spliterator);
		textActionsMap = new ConcurrentHashMap<>();
	}

	@Override
	public void register(Object object) {
		Stream.of(object.getClass().getMethods()).filter(m -> m.isAnnotationPresent(Text.class)).forEach(m -> {
				List<ArgumentType> arguments = getArgumentTypes(m);
				InstanceMethod instanceMethod = new InstanceMethod(object, m, arguments);
				Text text = m.getAnnotation(Text.class);

				if (text.states().length == 0) {
					addAction(ALL_STATES, instanceMethod);
				} else {
					for (String state : text.states()) {
						addAction(state, instanceMethod);
					}
				}
			});
	}

	public void processText(String text, ChatUser user) {
		InstanceMethod instanceMethod = findInstanceMethod(user);
		try {
			Object casted = user;
			Class<?>[] parameterTypes = instanceMethod.getMethod().getParameterTypes();
			for (Class<?> parameterType : parameterTypes) {
				if (ChatUser.class.isAssignableFrom(parameterType)) {
					casted = parameterType.cast(user);
				}
			}

			instanceMethod.getMethod().invoke(instanceMethod.getInstance(),
					getArguments(instanceMethod.getArgumentTypes(), text, casted, user.getState()));
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

	private Object[] getArguments(List<ArgumentType> types, String text, Object user, String state) {
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
				result[index] = ParametersUtils.getParameters(state, spliterator);
				break;
			default:
				throw new WrongMethodSignatureException("Wrong parameters");
			}

		});
		return result;

	}

}
