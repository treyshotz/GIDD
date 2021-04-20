package com.ntnu.gidd.exception;

public class PasswordIsIncorrectException extends RuntimeException {
	public PasswordIsIncorrectException() {
		super("Password is incorrect");
	}
}

