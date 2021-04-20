package com.ntnu.gidd.exception;

public class RefreshTokenNotFound extends RuntimeException {
    public RefreshTokenNotFound() {
        super("Refresh token not found");
    }
}
