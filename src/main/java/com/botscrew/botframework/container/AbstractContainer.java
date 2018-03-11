package com.botscrew.botframework.container;

import com.botscrew.botframework.annotation.Param;
import com.botscrew.botframework.exception.ProcessorInnerException;
import com.botscrew.botframework.model.ArgumentType;
import com.botscrew.botframework.model.ChatUser;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractContainer {

	protected final String spliterator;
	protected static final String ALL_STATES = "ALL_STATES";
	private static final String DEFAULT_SPLITERATOR = "?";

	private static final Map<Class, ArgumentType> supportedBaseTypes;

	static {
		supportedBaseTypes = new HashMap<>();

		supportedBaseTypes.put(Integer.class, ArgumentType.PARAM_INTEGER);
		supportedBaseTypes.put(Long.class, ArgumentType.PARAM_LONG);
		supportedBaseTypes.put(Byte.class, ArgumentType.PARAM_BYTE);
		supportedBaseTypes.put(Short.class, ArgumentType.PARAM_SHORT);
		supportedBaseTypes.put(Double.class, ArgumentType.PARAM_DOUBLE);
		supportedBaseTypes.put(Float.class, ArgumentType.PARAM_FLOAT);
		supportedBaseTypes.put(String.class, ArgumentType.PARAM_STRING);
	}

	public AbstractContainer() {
		this.spliterator = DEFAULT_SPLITERATOR;
	}

	AbstractContainer(String spliterator) {
		this.spliterator = spliterator;
	}

	List<ArgumentType> getArgumentTypes(Method m) {
		return Arrays.stream( m.getParameters())
				.map(this::getArgumentType)
				.collect(Collectors.toList());
	}

	Optional<ArgumentType> getBaseTypeArgumentByClass(Class<?> type) {
		return Optional.ofNullable(supportedBaseTypes.get(type));
	}

	String getParamName(Parameter parameter) {
		if (parameter.isNamePresent()) return parameter.getName();

		if (parameter.isAnnotationPresent(Param.class)) {
			return parameter.getAnnotation(Param.class).value();
		}
		return parameter.getName();
	}

	Object convertUser(ChatUser user, Parameter parameter) {
		if (ChatUser.class.isAssignableFrom(parameter.getType()))
			return parameter.getType().cast(user);

		throw new ProcessorInnerException("Class " + user.getClass().getName() +
				" is not implementation of ChatUser");
	}

	public abstract void register(Object object);

	protected abstract ArgumentType getArgumentType(Parameter parameter);

}
