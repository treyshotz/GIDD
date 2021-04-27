package com.ntnu.gidd.exception;

public class RegistrationNotFoundException extends EntityNotFoundException {
    private static final long serialVersionUID = 1L;

    public RegistrationNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public RegistrationNotFoundException(){
        super("Registration does not exist");
    }
}
