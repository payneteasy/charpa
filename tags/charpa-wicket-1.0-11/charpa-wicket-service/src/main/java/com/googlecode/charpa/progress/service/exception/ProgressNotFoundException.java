package com.googlecode.charpa.progress.service.exception;

/**
 * Thrown when a progress is not found (or cannot be obtained by the caller
 * because of security restrictions).
 */
public class ProgressNotFoundException extends RuntimeException {

	public ProgressNotFoundException() {
		super();
	}

	public ProgressNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProgressNotFoundException(String message) {
		super(message);
	}

	public ProgressNotFoundException(Throwable cause) {
		super(cause);
	}

}
