package com.ntnu.gidd.exception;

import com.ntnu.gidd.util.ValidationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionHandler {

      @ResponseStatus(HttpStatus.BAD_REQUEST)
      @ExceptionHandler(MethodArgumentNotValidException.class)
      public ValidationResponse handleValidationExceptions(MethodArgumentNotValidException exception){
            Map<String, String> errorMessages = new HashMap<>();

            exception.getBindingResult().getFieldErrors().forEach(error -> {
                  errorMessages.put(error.getField(), error.getDefaultMessage());
            });
            return new ValidationResponse("INVALID_METHOD_ARGUMENT", errorMessages);
      }
}
