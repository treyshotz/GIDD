package com.ntnu.gidd.exception;

public class ResetPasswordTokenNotFoundException extends EntityNotFoundException {
	public ResetPasswordTokenNotFoundException(String errorMessage) {
		super(errorMessage);
	}
	
	public ResetPasswordTokenNotFoundException() {
		super("Reset-password token was not found");
	}
}



