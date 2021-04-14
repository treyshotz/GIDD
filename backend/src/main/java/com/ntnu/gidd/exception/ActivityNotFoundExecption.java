package com.ntnu.gidd.exception;
import javax.persistence.EntityNotFoundException;

public class ActivityNotFoundExecption extends EntityNotFoundException{

    private static final long serialVersionUID = 1L;

    public ActivityNotFoundExecption(String errorMessage){
        super(errorMessage);
    }

}