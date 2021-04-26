package com.ntnu.gidd.exception;

public class InvalidUnInviteExecption extends RuntimeException {
    public InvalidUnInviteExecption() {
        super("You can't uninvite a user that is registered for the activity");
    }
}
