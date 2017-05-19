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

import com.botscrew.framework.flow.annotation.Location;
import com.botscrew.framework.flow.annotation.LocationProcessor;
import com.botscrew.framework.flow.exception.DuplicatedActionException;
import com.botscrew.framework.flow.exception.ProcessorInnerException;
import com.botscrew.framework.flow.exception.WrongMethodSignatureException;
import com.botscrew.framework.flow.model.ArgumentType;
import com.botscrew.framework.flow.model.ChatUser;
import com.botscrew.framework.flow.model.GeoCoordinates;
import com.botscrew.framework.flow.model.InstanceMethod;
import com.botscrew.framework.flow.util.ParametersUtils;
import com.botscrew.framework.flow.util.TypeChecker;

public class LocationContainer<U extends ChatUser> extends AbstractContainer {

	private final Map<String, InstanceMethod> locationActionsMap;

	public LocationContainer(String packageName) {
		super(packageName);
		locationActionsMap = new ConcurrentHashMap<>();
	}

	public LocationContainer(String packageName, String spliterator) {
		super(packageName, spliterator);
		locationActionsMap = new ConcurrentHashMap<>();
	}

	@PostConstruct
	public void init() {
		Reflections reflections = new Reflections(packageName);
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(LocationProcessor.class);

		annotated.forEach(c -> {
			Stream.of(c.getMethods()).filter(m -> m.isAnnotationPresent(Location.class)).forEach(m -> {

				InstanceMethod instanceMethod = new InstanceMethod(context.getBean(c), m, getArgumentTypes(m));
				Location location = m.getAnnotation(Location.class);

				if (location.states().length == 0) {
					addAction(ALL_STATES, instanceMethod);

				} else {
					for (String state : location.states()) {
						addAction(state, instanceMethod);
					}
				}

			});
		});
	}

	public void processLocation(GeoCoordinates coordinates, U user) {
		InstanceMethod instanceMethod = findInstanceMethod(user);

		try {
			instanceMethod.getMethod().invoke(instanceMethod.getInstance(),
					getArguments(instanceMethod.getArgumentTypes(), coordinates, user));
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
	protected ArgumentType getArgumentType(Class<?> type, Annotation[] annotations) {
		if (type.equals(GeoCoordinates.class)) {
			return ArgumentType.COORDINATES;
		}
		if (TypeChecker.isInterfaceImplementing(type, ChatUser.class)) {
			return ArgumentType.USER;
		}
		if (TypeChecker.isInterfaceImplementing(type, Map.class)) {
			return ArgumentType.STATE_PARAMETERS;
		}
		throw new WrongMethodSignatureException(
				"Methods can only contain next parameters: " + "Coordinates.class , ChatUser "
						+ "and Map<String,String> state parameters. All of these parameters are optional");
	}

	private Object[] getArguments(List<ArgumentType> types, GeoCoordinates coordinates, ChatUser user) {
		final Object[] result = new Object[types.size()];
		IntStream.range(0, types.size()).forEach(index -> {
			switch (types.get(index)) {
			case USER:
				result[index] = user;
				break;
			case COORDINATES:
				result[index] = coordinates;
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
