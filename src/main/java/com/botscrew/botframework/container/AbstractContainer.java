package com.botscrew.botframework.container;

import com.botscrew.botframework.domain.ArgumentKit;
import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.exception.ProcessorInnerException;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractContainer {

	void tryInvokeMethod(HandlingMethod instanceMethod, ArgumentKit kit) {
		try {
			Object instance = instanceMethod.getInstance();
			Object[] args = instanceMethod.composeArgs(kit);
			instanceMethod.getMethod().invoke(instance, args);
		}
		catch (IllegalAccessException | InvocationTargetException e) {
			throw new ProcessorInnerException("Cannot process instance method", e);
		}
	}

}
