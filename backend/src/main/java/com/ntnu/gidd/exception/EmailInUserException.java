package com.ntnu.gidd.exception;

public class EmailInUserException extends Exception {
	
	public Exception EmailInUseException(String error) {
		return new Exception(error);
	}
}
