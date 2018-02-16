package com.botscrew.framework.flow.exception;

public class DuplicatedActionException extends RuntimeException {

	public DuplicatedActionException() {
	}

	public DuplicatedActionException(String message) {
		super(message);
	}

	public DuplicatedActionException(Throwable cause) {
		super(cause);
	}

	public DuplicatedActionException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicatedActionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
