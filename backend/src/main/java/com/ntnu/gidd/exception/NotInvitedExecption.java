package com.ntnu.gidd.exception;

public class NotInvitedExecption extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "You can't uninvite a user that is registered for the activity";

    public NotInvitedExecption(String error) {
        super(error);
    }

    public NotInvitedExecption() {
        super(DEFAULT_MESSAGE);
    }
}
