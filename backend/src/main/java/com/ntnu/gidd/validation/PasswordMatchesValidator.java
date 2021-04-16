package com.ntnu.gidd.validation;

import com.ntnu.gidd.dto.UserRegistrationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

      Logger logger = LoggerFactory.getLogger(PasswordMatchesValidator.class);
      
      @Override
      public void initialize(PasswordMatches constraintAnnotation) {
      }
      @Override
      public boolean isValid(Object obj, ConstraintValidatorContext context){
            //UserRegistrationDto user = (UserRegistrationDto) obj;
            return true;
            //return user.getPassword().equals(user.getMatchingPassword());
      }
}