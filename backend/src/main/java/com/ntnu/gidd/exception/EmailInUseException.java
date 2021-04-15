package com.ntnu.gidd.exception;

public class EmailInUseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmailInUseException(String errorMessage) {
		super(errorMessage);
	}

	public EmailInUseException() {
		super("Email is already associated with another user");
	}
}
