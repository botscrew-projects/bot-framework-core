package com.botscrew.botframework.model;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class InstanceMethod {
	private Object instance;
	private Method method;
	private List<ArgumentType> argumentTypes;
	private List<Parameter> parameters;

	public InstanceMethod(Object instance, Method method) {
		super();
		this.instance = instance;
		this.method = method;
	}

	public InstanceMethod(Object instance, Method method, List<ArgumentType> argumentTypes, List<Parameter> parameters) {
		this.instance = instance;
		this.method = method;
		this.argumentTypes = argumentTypes;
		this.parameters = parameters;
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

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
}
