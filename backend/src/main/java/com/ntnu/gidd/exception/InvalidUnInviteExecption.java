package com.ntnu.gidd.exception;

public class InvalidUnInviteExecption extends RuntimeException {

    public static final String DEFAULT_MESSAGE = "You can't uninvite a user that is registered for the activity";

    public InvalidUnInviteExecption(String error) {
        super(error);
    }

    public InvalidUnInviteExecption() {
        super(DEFAULT_MESSAGE);
    }
}
