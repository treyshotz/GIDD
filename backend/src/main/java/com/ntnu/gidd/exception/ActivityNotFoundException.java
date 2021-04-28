package com.ntnu.gidd.exception;

public class ActivityNotFoundException extends EntityNotFoundException{

    public static final String DEFAULT_MESSAGE = "This activity does not exist";

    public ActivityNotFoundException(String errorMessage){
        super(errorMessage);
    }

    public ActivityNotFoundException(){
        super(DEFAULT_MESSAGE);
    }

}