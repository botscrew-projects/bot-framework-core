package com.botscrew.framework.flow.container;

import com.botscrew.framework.flow.model.ArgumentType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class AbstractContainer {

	protected final String spliterator;
	protected static final String ALL_STATES = "ALL_STATES";
	private static final String DEFAULT_SPLITERATOR = "?";

	public AbstractContainer() {
		this.spliterator = DEFAULT_SPLITERATOR;
	}

	protected AbstractContainer(String spliterator) {
		this.spliterator = spliterator;
	}

	protected List<ArgumentType> getArgumentTypes(Method m) {
		Class<?>[] parameterTypes = m.getParameterTypes();
		Annotation[][] parametersAnnotations = m.getParameterAnnotations();

		return IntStream.range(0, parameterTypes.length)
				.mapToObj(index -> getArgumentType(parameterTypes[index], parametersAnnotations[index]))
				.collect(Collectors.toList());
	}

	public abstract void registrate(Object object);

	protected abstract ArgumentType getArgumentType(Class<?> type, Annotation[] annotations);

}
