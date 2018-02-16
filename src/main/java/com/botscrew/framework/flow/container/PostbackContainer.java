package com.botscrew.framework.flow.container;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.botscrew.framework.flow.annotation.Postback;
import com.botscrew.framework.flow.annotation.PostbackParameters;
import com.botscrew.framework.flow.annotation.StateParameters;
import com.botscrew.framework.flow.exception.DuplicatedActionException;
import com.botscrew.framework.flow.exception.ProcessorInnerException;
import com.botscrew.framework.flow.exception.WrongMethodSignatureException;
import com.botscrew.framework.flow.model.ArgumentType;
import com.botscrew.framework.flow.model.ChatUser;
import com.botscrew.framework.flow.model.InstanceMethod;
import com.botscrew.framework.flow.model.PostbackStatesKey;
import com.botscrew.framework.flow.util.ParametersUtils;
import com.botscrew.framework.flow.util.TypeChecker;

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
	public void registrate(Object object) {
		Method[] methods = object.getClass().getMethods();
		for (Method m : methods) {
			if (m.isAnnotationPresent(Postback.class)) {
				List<ArgumentType> arguments = getArgumentTypes(m);
				InstanceMethod instanceMethod = new InstanceMethod(object, m, arguments);
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
	protected ArgumentType getArgumentType(Class<?> type, Annotation[] annotations) {
		if (type.equals(String.class)) {
			return ArgumentType.POSTBACK;
		}
		if (TypeChecker.isInterfaceImplementing(type, ChatUser.class)) {
			return ArgumentType.USER;
		}
		if (TypeChecker.isInterfaceImplementing(type, Map.class)) {
			Stream<Annotation> stream = Stream.of(annotations);
			if (stream.anyMatch(a -> a.annotationType().equals(PostbackParameters.class))) {
				return ArgumentType.POSTBACK_PARAMETERS;
			}
			stream = Stream.of(annotations);
			if (stream.anyMatch(a -> a.annotationType().equals(StateParameters.class))) {
				return ArgumentType.STATE_PARAMETERS;
			}
		}

		throw new WrongMethodSignatureException("Methods can only contains next parameters: "
				+ "String value of Postback, ChatUser, Map<String,String> postback parameters with annotation @PostbackParams"
				+ "and Map<String,String> state parameters with annotation @StateParams. All of these arguments are optional");
	}

	public void processPostback(String postback, ChatUser user) {
		String postbackValue = ParametersUtils.getValueWithoutParams(postback, spliterator);

		InstanceMethod instanceMethod = findInstanceMethod(postbackValue, user);

		Object casted = user;

		try {
			Class<?>[] parameterTypes = instanceMethod.getMethod().getParameterTypes();
			for (Class<?> parameterType : parameterTypes) {
				if (ChatUser.class.isAssignableFrom(parameterType)) {
					casted = parameterType.cast(user);
				}
			}

			instanceMethod.getMethod().invoke(instanceMethod.getInstance(),
					getArguments(instanceMethod.getArgumentTypes(), postback, casted, user.getState()));
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new ProcessorInnerException(e.getCause());
		}

	}

	private Object[] getArguments(List<ArgumentType> types, String postback, Object user, String state) {
		final Object[] result = new Object[types.size()];
		IntStream.range(0, types.size()).forEach(index -> {
			switch (types.get(index)) {
			case USER:
				result[index] = user;
				break;
			case POSTBACK:
				result[index] = ParametersUtils.getValueWithoutParams(postback, spliterator);
				break;
			case POSTBACK_PARAMETERS:
				result[index] = ParametersUtils.getParameters(postback, spliterator);
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
