package com.ntnu.gidd.exception;

import javax.persistence.EntityExistsException;

public class EmailInUseException extends EntityExistsException {

	private static final long serialVersionUID = 1L;

	public EmailInUseException(String errorMessage) {
		super(errorMessage);
	}

	public EmailInUseException() {
		super("Email is already associated with another user");
	}
}
