package com.botscrew.framework.flow.model;

import java.lang.reflect.Method;
import java.util.List;

public class InstanceMethod {
	private Object instance;
	private Method method;
	private List<ArgumentType> argumentTypes;

	public InstanceMethod(Object instance, Method method) {
		super();
		this.instance = instance;
		this.method = method;
	}

	public InstanceMethod(Object instance, Method method, List<ArgumentType> argumentTypes) {
		super();
		this.instance = instance;
		this.method = method;
		this.argumentTypes = argumentTypes;
	}

	public Object getInstance() {
		return instance;
	}

	public void setInstance(Object instance) {
		this.instance = instance;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public List<ArgumentType> getArgumentTypes() {
		return argumentTypes;
	}

	public void setArgumentTypes(List<ArgumentType> argumentTypes) {
		this.argumentTypes = argumentTypes;
	}

}
