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

import com.botscrew.framework.flow.annotation.Text;
import com.botscrew.framework.flow.annotation.TextProcessor;
import com.botscrew.framework.flow.exception.DuplicatedActionException;
import com.botscrew.framework.flow.exception.ProcessorInnerException;
import com.botscrew.framework.flow.exception.WrongMethodSignatureException;
import com.botscrew.framework.flow.model.ChatUser;
import com.botscrew.framework.flow.model.InstanceMethod;
import com.botscrew.framework.flow.util.TypeChecker;

public class TextContainer<U extends ChatUser> {

	@Autowired
	private ApplicationContext context;
	private static final String ALL_STATES = "ALL_STATES";
	private final Map<String, InstanceMethod> textActionsMap;
	private final String packageName;

	public TextContainer(String packageName) {
		textActionsMap = new ConcurrentHashMap<>();
		this.packageName = packageName;

	}

	@PostConstruct
	private void init() {
		Reflections reflections = new Reflections(packageName);
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(TextProcessor.class);

		annotated.forEach(clazz -> {
			Stream.of(clazz.getMethods()).filter(m -> m.isAnnotationPresent(Text.class)).forEach(m -> {
				checkParameters(m);
				InstanceMethod instanceMethod = new InstanceMethod(context.getBean(clazz), m);
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
		String state = user.getState();

		InstanceMethod instanceMethod = textActionsMap.get(state);
		if (instanceMethod == null) {
			instanceMethod = textActionsMap.get(ALL_STATES);
			if (instanceMethod == null) {
				throw new IllegalArgumentException("No method with annotation @Text which meets given parameters");
			}
		}

		try {
			instanceMethod.getMethod().invoke(instanceMethod.getInstance(), text, user);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new ProcessorInnerException(e.getCause());
		}

	}

	private void addAction(String state, InstanceMethod instanceMethod) {
		if (textActionsMap.containsKey(state)) {
			throw new DuplicatedActionException("Duplication of text processing action: state = " + state);
		}
		textActionsMap.put(state, instanceMethod);
	}

	private void checkParameters(Method m) {
		Class<?>[] parameterTypes = m.getParameterTypes();
		if (parameterTypes.length != 2 || !parameterTypes[0].equals(String.class)
				|| !TypeChecker.isInterfaceImplementing(parameterTypes[1], ChatUser.class)) {
			throw new WrongMethodSignatureException(
					"Method should have 2 arguments: text - string value , instance of class which implements ChatUser ");
		}
	}

}
