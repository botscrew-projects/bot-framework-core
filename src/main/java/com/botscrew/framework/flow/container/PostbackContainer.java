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

import com.botscrew.framework.flow.annotation.Postback;
import com.botscrew.framework.flow.annotation.PostbackProcessor;
import com.botscrew.framework.flow.exception.DuplicatedActionException;
import com.botscrew.framework.flow.exception.ProcessorInnerException;
import com.botscrew.framework.flow.exception.WrongMethodSignatureException;
import com.botscrew.framework.flow.model.ChatUser;
import com.botscrew.framework.flow.model.InstanceMethod;
import com.botscrew.framework.flow.model.PostbackStatesKey;
import com.botscrew.framework.flow.util.TypeChecker;

public class PostbackContainer<U extends ChatUser> {
	@Autowired
	private ApplicationContext context;

	private static final String ALL_STATES = "ALL_STATES";
	private static final String DEFAULT_POSTBACK = "DEFAULT_POSTBACK";
	private static final String DEFAULT_SPLITERATOR = "?";
	private final Map<PostbackStatesKey, InstanceMethod> postbackActionsMap;
	private final String packageName;
	private final String spliterator;

	public PostbackContainer(String packageName) {
		this.postbackActionsMap = new ConcurrentHashMap<>();
		this.packageName = packageName;
		this.spliterator = DEFAULT_SPLITERATOR;
	}

	public PostbackContainer(String packageName, String spliterator) {
		this.postbackActionsMap = new ConcurrentHashMap<>();
		this.packageName = packageName;
		this.spliterator = spliterator;
	}

	@PostConstruct
	private void init() {
		Reflections reflections = new Reflections(packageName);
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(PostbackProcessor.class);

		annotated.forEach(c -> {
			Stream.of(c.getMethods()).filter(m -> m.isAnnotationPresent(Postback.class)).forEach(m -> {
				checkParameters(m);
				InstanceMethod instanceMethod = new InstanceMethod(context.getBean(c), m);
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
			});
		});
	}

	private void addAction(PostbackStatesKey postbackStatesKey, InstanceMethod instanceMethod) {
		if (postbackActionsMap.containsKey(postbackStatesKey)) {
			throw new DuplicatedActionException("Duplication of postback processing action: postback = "
					+ postbackStatesKey.getPostback() + ", " + postbackStatesKey.getState());
		}
		postbackActionsMap.put(postbackStatesKey, instanceMethod);
	}

	private void checkParameters(Method m) {
		Class<?>[] parameterTypes = m.getParameterTypes();
		if (parameterTypes.length != 2 || !parameterTypes[0].equals(String.class)
				|| !TypeChecker.isInterfaceImplementing(parameterTypes[1], ChatUser.class)) {
			throw new WrongMethodSignatureException(
					"Method should have 2 arguments: instance of String, instance of class which implements ChatUser ");
		}
	}

	public void processPostback(String postback, U user) {
		String state = user.getState();
		String postbackValue = postback;
		if (postbackValue.contains(spliterator)) {
			postbackValue = postbackValue.substring(0, postbackValue.indexOf(spliterator));
		}
		InstanceMethod instanceMethod = findInstanceMethod(postbackValue, state);

		try {
			instanceMethod.getMethod().invoke(instanceMethod.getInstance(), postback, user);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new ProcessorInnerException(e.getCause());
		}

	}

	private InstanceMethod findInstanceMethod(String postbackValue, String state) {
		InstanceMethod instanceMethod = postbackActionsMap.get(new PostbackStatesKey(postbackValue, state));
		if (instanceMethod == null) {
			instanceMethod = postbackActionsMap.get(new PostbackStatesKey(postbackValue, ALL_STATES));
			if (instanceMethod == null) {
				instanceMethod = postbackActionsMap.get(new PostbackStatesKey(DEFAULT_POSTBACK, state));
				if (instanceMethod == null) {
					instanceMethod = postbackActionsMap.get(new PostbackStatesKey(DEFAULT_POSTBACK, ALL_STATES));
				}
				if (instanceMethod == null) {
					throw new IllegalArgumentException(
							"No method with annotation @Postback which meets given parameters");
				}
			}

		}
		return instanceMethod;
	}

}
