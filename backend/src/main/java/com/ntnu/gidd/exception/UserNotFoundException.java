package com.ntnu.gidd.exception;


public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(String errorMessage){
        super(errorMessage);
    }

    public UserNotFoundException(){ super("User was not found");}
}

