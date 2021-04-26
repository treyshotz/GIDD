package com.ntnu.gidd.exception;

public class InvalidResetPasswordToken extends RuntimeException {
	public InvalidResetPasswordToken() {
		super("Invalid reset password token");
	}
}

