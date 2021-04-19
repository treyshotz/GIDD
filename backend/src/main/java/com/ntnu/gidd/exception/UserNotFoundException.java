package com.ntnu.gidd.exception;
import javax.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException{
	
	private static final long serialVersionUID = 1L;
	
	public UserNotFoundException(String errorMessage){
		super(errorMessage);
	}

	public UserNotFoundException(){ super("User was not found");}
	
}