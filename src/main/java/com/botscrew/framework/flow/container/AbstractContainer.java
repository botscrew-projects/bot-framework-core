package com.botscrew.framework.flow.container;

import com.botscrew.framework.flow.model.ArgumentType;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractContainer {

	protected final String spliterator;
	protected static final String ALL_STATES = "ALL_STATES";
	private static final String DEFAULT_SPLITERATOR = "?";

	private static final Map<Class, ArgumentType> supportedArguments;

	static {
		supportedArguments = new HashMap<>();

		supportedArguments.put(Integer.class, ArgumentType.PARAM_INTEGER);
		supportedArguments.put(Long.class, ArgumentType.PARAM_LONG);
		supportedArguments.put(Byte.class, ArgumentType.PARAM_BYTE);
		supportedArguments.put(Short.class, ArgumentType.PARAM_SHORT);
		supportedArguments.put(Double.class, ArgumentType.PARAM_DOUBLE);
		supportedArguments.put(Float.class, ArgumentType.PARAM_FLOAT);
		supportedArguments.put(String.class, ArgumentType.PARAM_STRING);
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

	public Optional<ArgumentType> getArgumentTypeByClass(Class<?> type) {
		return Optional.ofNullable(supportedArguments.get(type));
	}

	public abstract void register(Object object);

	protected abstract ArgumentType getArgumentType(Parameter parameter);

}
