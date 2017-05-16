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

import com.botscrew.framework.flow.annotation.MessageSender;
import com.botscrew.framework.flow.annotation.NextMessage;
import com.botscrew.framework.flow.exception.DuplicatedActionException;
import com.botscrew.framework.flow.exception.WrongMethodSignatureException;
import com.botscrew.framework.flow.model.ChatUser;
import com.botscrew.framework.flow.model.InstanceMethod;
import com.botscrew.framework.flow.util.TypeChecker;

public class NextMessagesContainer<U extends ChatUser> {

	private static final String ALL_STATES = "ALL_STATES";
	private final Map<String, InstanceMethod> nextMessagesMap;
	private final String packageName;
	@Autowired
	private ApplicationContext context;

	public NextMessagesContainer(String packageName) {
		nextMessagesMap = new ConcurrentHashMap<>();
		this.packageName = packageName;
	}

	@PostConstruct
	public void init() {
		Reflections reflections = new Reflections(packageName);
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(MessageSender.class);

		annotated.forEach(c -> {
			Stream.of(c.getMethods()).filter(m -> m.isAnnotationPresent(NextMessage.class)).forEach(m -> {
				checkParameters(m);
				InstanceMethod instanceMethod = new InstanceMethod(context.getBean(c), m);
				NextMessage nextMessage = m.getAnnotation(NextMessage.class);

				if (nextMessage.states().length == 0) {
					addAction(ALL_STATES, instanceMethod);
				} else {
					for (String state : nextMessage.states()) {
						addAction(state, instanceMethod);
					}
				}
			});
		});
	}

	public void sendNextMessage(U user) {
		String state = user.getState();

		InstanceMethod instanceMethod = nextMessagesMap.get(state);
		if (instanceMethod == null) {
			instanceMethod = nextMessagesMap.get(ALL_STATES);
			if (instanceMethod == null) {
				throw new IllegalArgumentException(
						"No method with annotation @NextMessage which meets given parameters");
			}
		}

		try {
			instanceMethod.getMethod().invoke(instanceMethod.getInstance(), user);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new IllegalArgumentException(e.getCause());
		}

	}

	private void addAction(String state, InstanceMethod instanceMethod) {
		if (nextMessagesMap.containsKey(state)) {
			throw new DuplicatedActionException("Duplication of next message action: state = " + state);
		}
		nextMessagesMap.put(state, instanceMethod);
	}

	private void checkParameters(Method m) {
		Class<?>[] parameterTypes = m.getParameterTypes();
		if (parameterTypes.length != 1 || !TypeChecker.isInterfaceImplementing(parameterTypes[0], ChatUser.class)) {
			throw new WrongMethodSignatureException(
					"Method should have 1 argument: instance of class which implements ChatUser ");
		}
	}

}
