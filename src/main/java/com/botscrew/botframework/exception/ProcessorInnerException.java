package com.botscrew.botframework.exception;

public class ProcessorInnerException extends RuntimeException {

	public ProcessorInnerException() {
		super();
	}

	public ProcessorInnerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProcessorInnerException(String message) {
		super(message);
	}

	public ProcessorInnerException(Throwable cause) {
		super(cause);
	}

}
