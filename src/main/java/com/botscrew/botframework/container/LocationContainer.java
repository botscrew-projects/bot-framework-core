package com.botscrew.botframework.container;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.botscrew.botframework.annotation.Location;
import com.botscrew.botframework.exception.DuplicatedActionException;
import com.botscrew.botframework.exception.ProcessorInnerException;
import com.botscrew.botframework.exception.WrongMethodSignatureException;
import com.botscrew.botframework.model.ArgumentType;
import com.botscrew.botframework.model.GeoCoordinates;
import com.botscrew.botframework.model.InstanceMethod;
import com.botscrew.botframework.util.ParametersUtils;
import com.botscrew.botframework.model.ChatUser;

public class LocationContainer extends AbstractContainer {

	private final Map<String, InstanceMethod> locationActionsMap;

	public LocationContainer() {
		locationActionsMap = new ConcurrentHashMap<>();
	}

	public LocationContainer(String spliterator) {
		super(spliterator);
		locationActionsMap = new ConcurrentHashMap<>();
	}

	@Override
	public void register(Object object) {
		Stream.of(object.getClass().getMethods())
				.filter(m -> m.isAnnotationPresent(Location.class))
				.forEach(m -> {
			InstanceMethod instanceMethod = new InstanceMethod(object, m, getArgumentTypes(m), Arrays.asList(m.getParameters()));
			Location location = m.getAnnotation(Location.class);

			if (location.states().length == 0) {
				addAction(ALL_STATES, instanceMethod);
			} else {
				for (String state : location.states()) {
					addAction(state, instanceMethod);
				}
			}
		});
	}

	public void processLocation(GeoCoordinates coordinates, ChatUser user) {
		InstanceMethod instanceMethod = findInstanceMethod(user);
		Object[] arguments = getArguments(instanceMethod.getArgumentTypes(), instanceMethod.getParameters(), coordinates, user);
		try {
			instanceMethod.getMethod().invoke(instanceMethod.getInstance(), arguments);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new ProcessorInnerException(e.getCause());
		}

	}

	private InstanceMethod findInstanceMethod(ChatUser user) {
		String stateValue = ParametersUtils.getValueWithoutParams(user.getState(), spliterator);

		InstanceMethod instanceMethod = locationActionsMap.get(stateValue);
		if (instanceMethod == null) {
			instanceMethod = locationActionsMap.get(ALL_STATES);
			if (instanceMethod == null) {
				throw new IllegalArgumentException(
						"No method with annotation @Location which meets given parameters, state:" + user.getState());
			}
		}
		return instanceMethod;
	}

	private void addAction(String state, InstanceMethod instanceMethod) {
		if (locationActionsMap.containsKey(state)) {
			throw new DuplicatedActionException("Duplication of location processing action: state = " + state);
		}
		locationActionsMap.put(state, instanceMethod);
	}

	@Override
	protected ArgumentType getArgumentType(Parameter parameter) {
		Class<?> type = parameter.getType();

		if (type.equals(GeoCoordinates.class)) return ArgumentType.COORDINATES;

		if (ChatUser.class.isAssignableFrom(parameter.getType())) return ArgumentType.USER;

		if (Map.class.isAssignableFrom(parameter.getType())) return ArgumentType.STATE_PARAMETERS;

		Optional<ArgumentType> argumentTypeOpt = getArgumentTypeByClass(parameter.getType());

		return argumentTypeOpt.orElseThrow(() -> new WrongMethodSignatureException(
				"Methods can only contain next parameters: \n" +
						"ChatUser implementation, Map, String, Long, Integer, Short, Byte, Double, Float"));
	}

	private Object[] getArguments(List<ArgumentType> types, List<Parameter> parameters, GeoCoordinates coordinates, ChatUser user) {
		final Object[] result = new Object[types.size()];
		Map<String, String> stateParameters = ParametersUtils.getParameters(user.getState(), spliterator);

		IntStream.range(0, types.size()).forEach(index -> {

			switch (types.get(index)) {
				case USER:
					result[index] = convertUser(user, parameters.get(index));
					break;
				case COORDINATES:
					result[index] = coordinates;
					break;
				case STATE_PARAMETERS:
					result[index] = stateParameters;
					break;
				case PARAM_STRING:
					String name = getParamName(parameters.get(index));
					result[index] = stateParameters.get(name);
					break;
				case PARAM_BYTE:
					name = getParamName(parameters.get(index));
					result[index] = Byte.valueOf(stateParameters.get(name));
					break;
				case PARAM_SHORT:
					name = getParamName(parameters.get(index));
					result[index] = Short.valueOf(stateParameters.get(name));
					break;
				case PARAM_INTEGER:
					name = getParamName(parameters.get(index));
					result[index] = Integer.valueOf(stateParameters.get(name));
					break;
				case PARAM_LONG:
					name = getParamName(parameters.get(index));
					result[index] = Long.valueOf(stateParameters.get(name));
					break;
				case PARAM_FLOAT:
					name = getParamName(parameters.get(index));
					result[index] = Float.valueOf(stateParameters.get(name));
					break;
				case PARAM_DOUBLE:
					name = getParamName(parameters.get(index));
					result[index] = Double.valueOf(stateParameters.get(name));
					break;
				default:
					result[index] = null;
			}

		});
		return result;
	}

}
