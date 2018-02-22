package com.botscrew.botframework.exception;

public class WrongMethodSignatureException extends RuntimeException {

	public WrongMethodSignatureException() {
		super();
	}

	public WrongMethodSignatureException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public WrongMethodSignatureException(String message, Throwable cause) {
		super(message, cause);
	}

	public WrongMethodSignatureException(String message) {
		super(message);
	}

	public WrongMethodSignatureException(Throwable cause) {
		super(cause);
	}

}
