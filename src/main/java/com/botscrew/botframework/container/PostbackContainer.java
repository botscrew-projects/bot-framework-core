package com.botscrew.botframework.container;

import com.botscrew.botframework.annotation.Postback;
import com.botscrew.botframework.annotation.PostbackParameters;
import com.botscrew.botframework.annotation.StateParameters;
import com.botscrew.botframework.exception.DuplicatedActionException;
import com.botscrew.botframework.exception.ProcessorInnerException;
import com.botscrew.botframework.exception.WrongMethodSignatureException;
import com.botscrew.botframework.model.ArgumentType;
import com.botscrew.botframework.model.ChatUser;
import com.botscrew.botframework.model.InstanceMethod;
import com.botscrew.botframework.model.PostbackStatesKey;
import com.botscrew.botframework.util.ParametersUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class PostbackContainer extends AbstractContainer {
	private static final String DEFAULT_POSTBACK = "DEFAULT_POSTBACK";
	private final Map<PostbackStatesKey, InstanceMethod> postbackActionsMap;

	public PostbackContainer() {
		super();
		this.postbackActionsMap = new ConcurrentHashMap<>();
	}

	public PostbackContainer(String spliterator) {
		super(spliterator);
		this.postbackActionsMap = new ConcurrentHashMap<>();
	}

	@Override
	public void register(Object object) {
		Method[] methods = object.getClass().getMethods();
		for (Method m : methods) {
			if (m.isAnnotationPresent(Postback.class)) {
				List<ArgumentType> arguments = getArgumentTypes(m);
				InstanceMethod instanceMethod = new InstanceMethod(object, m, arguments, Arrays.asList(m.getParameters()));
				Postback postback = m.getAnnotation(Postback.class);
				String postbackValue = postback.postback();
				if (postbackValue.isEmpty()) {
					postbackValue = DEFAULT_POSTBACK;
				}

				if (postback.states().length == 0) {
					addAction(new PostbackStatesKey(postbackValue, ALL_STATES), instanceMethod);

				} else {
					for (String state : postback.states()) {
						addAction(new PostbackStatesKey(postbackValue, state), instanceMethod);
					}
				}
			}
		}
	}

	private void addAction(PostbackStatesKey postbackStatesKey, InstanceMethod instanceMethod) {
		if (postbackActionsMap.containsKey(postbackStatesKey)) {
			throw new DuplicatedActionException("Duplication of postback processing action: postback = "
					+ postbackStatesKey.getPostback() + ", " + postbackStatesKey.getState());
		}
		postbackActionsMap.put(postbackStatesKey, instanceMethod);
	}

	@Override
	protected ArgumentType getArgumentType(Parameter parameter) {
		if (parameter.isAnnotationPresent(Postback.class)) return ArgumentType.POSTBACK;

		if (ChatUser.class.isAssignableFrom(parameter.getType())) return ArgumentType.USER;

		Class<?> type = parameter.getType();
		if (Map.class.isAssignableFrom(type)) {
			if (type.isAnnotationPresent(PostbackParameters.class)) return ArgumentType.POSTBACK_PARAMETERS;
			if (type.isAnnotationPresent(StateParameters.class)) return ArgumentType.STATE_PARAMETERS;
		}

		Optional<ArgumentType> argumentTypeOpt = getBaseTypeArgumentByClass(parameter.getType());

		return argumentTypeOpt.orElseThrow(() -> new WrongMethodSignatureException(
				"Methods can only contain next parameters: \n" +
						"ChatUser implementation, Map, String, Long, Integer, Short, Byte, Double, Float"));
	}

	public void processPostback(String postback, ChatUser user) {
		String postbackValue = ParametersUtils.getValueWithoutParams(postback, spliterator);

		InstanceMethod instanceMethod = findInstanceMethod(postbackValue, user);
		Object[] arguments = getArguments(instanceMethod.getArgumentTypes(), instanceMethod.getParameters(), postback, user);
		try {
			instanceMethod.getMethod().invoke(instanceMethod.getInstance(), arguments);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new ProcessorInnerException(e.getCause());
		}
	}

	private Object[] getArguments(List<ArgumentType> types, List<Parameter> parameters, String postback, ChatUser user) {
		final Object[] result = new Object[types.size()];

		Map<String, String> stateParameters = ParametersUtils.getParameters(user.getState(), spliterator);
		Map<String, String> postbackParameters = ParametersUtils.getParameters(postback, spliterator);

		Map<String, String> allParameters = new HashMap<>(stateParameters);
		allParameters.putAll(postbackParameters);

		IntStream.range(0, types.size()).forEach(index -> {

			switch (types.get(index)) {
				case USER:
					result[index] = convertUser(user, parameters.get(index));
					break;
				case POSTBACK:
					result[index] = postback;
					break;
				case STATE_PARAMETERS:
					result[index] = stateParameters;
					break;
				case POSTBACK_PARAMETERS:
					result[index] = postbackParameters;
					break;
				case PARAM_STRING:
					String name = getParamName(parameters.get(index));
					result[index] = allParameters.get(name);
					break;
				case PARAM_BYTE:
					name = getParamName(parameters.get(index));
					result[index] = Byte.valueOf(allParameters.get(name));
					break;
				case PARAM_SHORT:
					name = getParamName(parameters.get(index));
					result[index] = Short.valueOf(allParameters.get(name));
					break;
				case PARAM_INTEGER:
					name = getParamName(parameters.get(index));
					result[index] = Integer.valueOf(allParameters.get(name));
					break;
				case PARAM_LONG:
					name = getParamName(parameters.get(index));
					result[index] = Long.valueOf(allParameters.get(name));
					break;
				case PARAM_FLOAT:
					name = getParamName(parameters.get(index));
					result[index] = Float.valueOf(allParameters.get(name));
					break;
				case PARAM_DOUBLE:
					name = getParamName(parameters.get(index));
					result[index] = Double.valueOf(allParameters.get(name));
					break;
				default:
					result[index] = null;
			}

		});
		return result;
	}

	private InstanceMethod findInstanceMethod(String postbackValue, ChatUser user) {
		String stateValue = ParametersUtils.getValueWithoutParams(user.getState(), spliterator);

		InstanceMethod instanceMethod = postbackActionsMap.get(new PostbackStatesKey(postbackValue, stateValue));
		if (instanceMethod == null) {
			instanceMethod = postbackActionsMap.get(new PostbackStatesKey(postbackValue, ALL_STATES));
			if (instanceMethod == null) {
				instanceMethod = postbackActionsMap.get(new PostbackStatesKey(DEFAULT_POSTBACK, stateValue));
				if (instanceMethod == null) {
					instanceMethod = postbackActionsMap.get(new PostbackStatesKey(DEFAULT_POSTBACK, ALL_STATES));
				}
				if (instanceMethod == null) {
					throw new IllegalArgumentException(
							"No method with annotation @Postback which meets given parameters: postback:"
									+ postbackValue + ", state:" + stateValue);
				}
			}

		}
		return instanceMethod;
	}

}
