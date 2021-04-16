package com.ntnu.gidd.exception;


public class UserNotFoundExecption extends RuntimeException {

    public UserNotFoundExecption(String errorMessage){
        super(errorMessage);
    }

    public UserNotFoundExecption(){
        super("This User does not exist");
    }
}
