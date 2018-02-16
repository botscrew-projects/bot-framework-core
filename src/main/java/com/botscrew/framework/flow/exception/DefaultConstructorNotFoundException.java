package com.botscrew.framework.flow.exception;

public class DefaultConstructorNotFoundException extends RuntimeException {

	public DefaultConstructorNotFoundException() {
	}

	public DefaultConstructorNotFoundException(String message) {
		super(message);
	}

	public DefaultConstructorNotFoundException(Throwable cause) {
		super(cause);
	}

	public DefaultConstructorNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public DefaultConstructorNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
