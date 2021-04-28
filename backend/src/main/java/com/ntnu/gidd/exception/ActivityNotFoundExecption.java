package com.ntnu.gidd.exception;

public class ActivityNotFoundExecption extends EntityNotFoundException{

    public static final String DEFAULT_MESSAGE = "This activity does not exist";

    public ActivityNotFoundExecption(String errorMessage){
        super(errorMessage);
    }

    public ActivityNotFoundExecption(){
        super(DEFAULT_MESSAGE);
    }

}