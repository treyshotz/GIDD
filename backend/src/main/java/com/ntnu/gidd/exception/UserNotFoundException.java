package com.ntnu.gidd.exception;


public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String errorMessage){
        super(errorMessage);
    }

    public UserNotFoundException(){ super("User was not found");}
}

