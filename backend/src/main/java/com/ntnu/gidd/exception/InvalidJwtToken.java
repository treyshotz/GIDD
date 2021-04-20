package com.ntnu.gidd.exception;

public class InvalidJwtToken extends RuntimeException {
    public InvalidJwtToken() {
        super("Invalid Jwt token");
    }
}
