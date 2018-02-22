package com.botscrew.framework.flow.container;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
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

public class TextContainer extends AbstractContainer {

	private final Map<String, InstanceMethod> textActionsMap;
	private final Map<ArgumentType, BiFunction<Parameter, String, Object>> parameterConverters;

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
				InstanceMethod instanceMethod = new InstanceMethod(object, m, arguments, Arrays.asList(m.getParameters()));
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
			invoke(instanceMethod, text, user);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new ProcessorInnerException(e.getCause());
		}
	}

	private void invoke(InstanceMethod method, String text, ChatUser user) {

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
	protected ArgumentType getArgumentType(Parameter parameter) {
		if (parameter.getName().equals("text") && parameter.getType().equals(String.class)) return  ArgumentType.TEXT;

		if (ChatUser.class.isAssignableFrom(parameter.getType())) return ArgumentType.USER;

		if (Map.class.isAssignableFrom(parameter.getType())) return ArgumentType.STATE_PARAMETERS;

		Optional<ArgumentType> argumentTypeOpt = getArgumentTypeByClass(parameter.getType());

		return argumentTypeOpt.orElseThrow(() -> new WrongMethodSignatureException(
                "Methods can only contain next parameters: \n" +
						"ChatUser implementation, Map, String, Long, Integer, Short, Byte, Double, Float"));
	}

	private Object[] getArguments(List<ArgumentType> types, String text, ChatUser user) {
		final Object[] result = new Object[types.size()];
		Map<String, String> parameters = ParametersUtils.getParameters(user.getState(), spliterator);

		IntStream.range(0, types.size()).forEach(index -> {
			switch (types.get(index)) {
			case USER:
				result[index] = user;
				break;
			case TEXT:
				result[index] = text;
				break;
			case STATE_PARAMETERS:
				result[index] = parameters;
				break;
				case PARAM_STRING:
					result[index] =
			default:
				result[index] = null;
			}

		});
		return result;

	}

}
