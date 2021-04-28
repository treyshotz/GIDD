package com.ntnu.gidd.exception;

import com.ntnu.gidd.util.Response;
import com.ntnu.gidd.util.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

      @ResponseStatus(HttpStatus.BAD_REQUEST)
      @ExceptionHandler(MethodArgumentNotValidException.class)
      public ExceptionResponse handleValidationExceptions(MethodArgumentNotValidException exception){
            Map<String, String> errorMessages = new HashMap<>();

            exception.getBindingResult().getFieldErrors().forEach(error -> {
                  errorMessages.put(error.getField(), error.getDefaultMessage());
            });

            String message = "One or more method arguments is invalid";

            log.error("[X] Error caught {}", message);
            return new ExceptionResponse(message, errorMessages);
      }

      @ResponseStatus(value = HttpStatus.NOT_FOUND)
      @ExceptionHandler({ActivityNotFoundExecption.class, UserNotFoundException.class, RegistrationNotFoundException.class, RefreshTokenNotFound.class})
      public Response handleEntityNotFound(EntityNotFoundException exception){
            log.error("[X] Error caught while processing request {}", exception.getMessage());
            return new Response(exception.getMessage());
      }

      @ResponseStatus(value = HttpStatus.FORBIDDEN)
      @ExceptionHandler(NotInvitedExecption.class)
      public Response handleNotInvited(NotInvitedExecption exception){
            log.error("[X] Error caught while processing request {}", exception.getMessage());
            return new Response(exception.getMessage());
      }

      @ResponseStatus(value = HttpStatus.FORBIDDEN)
      @ExceptionHandler(InvalidUnInviteExecption.class)
      public Response handleNotAbleToUnInvite(InvalidUnInviteExecption exception){
            log.error("[X] Error caught while processing request {}", exception.getMessage());
            return new Response(exception.getMessage());
      }
}
