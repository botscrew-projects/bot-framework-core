package com.botscrew.framework.flow.container;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.botscrew.framework.flow.annotation.Location;
import com.botscrew.framework.flow.annotation.LocationProcessor;
import com.botscrew.framework.flow.exception.DuplicatedActionException;
import com.botscrew.framework.flow.exception.ProcessorInnerException;
import com.botscrew.framework.flow.exception.WrongMethodSignatureException;
import com.botscrew.framework.flow.model.ChatUser;
import com.botscrew.framework.flow.model.InstanceMethod;
import com.botscrew.framework.flow.util.TypeChecker;

public class LocationContainer<U extends ChatUser> {
	@Autowired
	private ApplicationContext context;

	private static final String ALL_STATES = "ALL_STATES";
	private final Map<String, InstanceMethod> locationActionsMap;
	private final String packageName;

	public LocationContainer(String packageName) {
		this.packageName = packageName;
		locationActionsMap = new ConcurrentHashMap<>();
	}

	@PostConstruct
	public void init() {
		Reflections reflections = new Reflections(packageName);
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(LocationProcessor.class);

		annotated.forEach(c -> {
			Stream.of(c.getMethods()).filter(m -> m.isAnnotationPresent(Location.class)).forEach(m -> {
				checkParameters(m);
				InstanceMethod instanceMethod = new InstanceMethod(context.getBean(c), m);
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

	public void processLocation(double latitude, double longitude, U user) {
		String state = user.getState();

		InstanceMethod instanceMethod = locationActionsMap.get(state);
		if (instanceMethod == null) {
			instanceMethod = locationActionsMap.get(ALL_STATES);
			if (instanceMethod == null) {
				throw new IllegalArgumentException("No method with annotation @Location which meets given parameters");
			}
		}

		try {
			instanceMethod.getMethod().invoke(instanceMethod.getInstance(), latitude, longitude, user);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new ProcessorInnerException(e.getCause());
		}

	}

	private void addAction(String state, InstanceMethod instanceMethod) {
		if (locationActionsMap.containsKey(state)) {
			throw new DuplicatedActionException("Duplication of location processing action: state = " + state);
		}
		locationActionsMap.put(state, instanceMethod);
	}

	private void checkParameters(Method m) {
		Class<?>[] parameterTypes = m.getParameterTypes();
		if (parameterTypes.length != 3 || !parameterTypes[0].equals(double.class)
				|| !parameterTypes[1].equals(double.class)
				|| !TypeChecker.isInterfaceImplementing(parameterTypes[2], ChatUser.class)) {
			throw new WrongMethodSignatureException(
					"Method should have 3 arguments: double value of latitude and longitude , instance of class which implements ChatUser ");
		}
	}

}
