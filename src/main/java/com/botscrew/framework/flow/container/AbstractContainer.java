package com.botscrew.framework.flow.container;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.botscrew.framework.flow.model.ArgumentType;

public abstract class AbstractContainer {
	@Autowired
	protected ApplicationContext context;
	protected final String packageName;
	protected final String spliterator;
	protected static final String ALL_STATES = "ALL_STATES";
	private static final String DEFAULT_SPLITERATOR = "?";

	protected AbstractContainer(String packageName) {
		this.packageName = packageName;
		this.spliterator = DEFAULT_SPLITERATOR;
	}

	protected AbstractContainer(String packageName, String spliterator) {
		this.packageName = packageName;
		this.spliterator = spliterator;
	}

	protected List<ArgumentType> getArgumentTypes(Method m) {
		Class<?>[] parameterTypes = m.getParameterTypes();
		Annotation[][] parametersAnnotations = m.getParameterAnnotations();

		return IntStream.range(0, parameterTypes.length).mapToObj(index -> {
			return getArgumentType(parameterTypes[index], parametersAnnotations[index]);
		}).collect(Collectors.toList());
	}

	abstract protected ArgumentType getArgumentType(Class<?> type, Annotation[] annotations);

}
