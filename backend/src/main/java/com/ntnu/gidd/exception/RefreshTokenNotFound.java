package com.ntnu.gidd.exception;

public class RefreshTokenNotFound extends EntityNotFoundException {
    public RefreshTokenNotFound() {
        super("Refresh token not found");
    }
}
