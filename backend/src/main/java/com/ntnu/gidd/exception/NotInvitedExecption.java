package com.ntnu.gidd.exception;

public class NotInvitedExecption extends RuntimeException {
    public NotInvitedExecption() {
        super("This user is not invited to this private activity");
    }

}
