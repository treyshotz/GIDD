package com.ntnu.gidd.exception;

public class RefreshTokenNotFound extends EntityNotFoundException {

    public static final String DEFAULT_MESSAGE = "Refresh token not found";

    public RefreshTokenNotFound(String error) {
        super(error);
    }

    public RefreshTokenNotFound() {
        super(DEFAULT_MESSAGE);
    }
}
