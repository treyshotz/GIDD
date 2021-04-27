package com.ntnu.gidd.exception;

public class ActivityNotFoundExecption extends EntityNotFoundException{


    public ActivityNotFoundExecption(String errorMessage){
        super(errorMessage);
    }

    public ActivityNotFoundExecption(){
        super("This activity does not exist");
    }

}