package com.ntnu.gidd.controller;

import com.ntnu.gidd.exception.RegistrationNotFoundException;
import com.ntnu.gidd.model.Registration;
import java.util.List;
import java.util.UUID;

import com.ntnu.gidd.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ntnu.gidd.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("users/{userId}/registrations/")
public class UserRegistrationController {

  @Autowired
  private RegistrationService registrationService;

  @GetMapping("")
  @ResponseStatus(HttpStatus.OK)
  public List<Registration> getRegistrationsForUser(@PathVariable UUID userId) {
    log.debug("[X] Request to look up acivites registered for user with id={}", userId);
    return registrationService.getRegistrationsForUser(userId);
  }

  @GetMapping("{activityId}/")
  @ResponseStatus(HttpStatus.OK)
  public Registration getRegistrationWithCompositeIdUser(@PathVariable UUID userId, @PathVariable UUID activityId) {
    return registrationService.getRegistrationWithCompositeId(userId, activityId);
  }

  @DeleteMapping("{activityId}/")
  @ResponseStatus(HttpStatus.OK)
  public Response deleteRegistration(@PathVariable UUID activityId, UUID userId){
    try {
      log.debug("[X] Request to delete Registration with activityId={} ", activityId);
      registrationService.deleteRegistrationWithCompositeId(userId, activityId);
      return new Response("Registration has been deleted");
    } catch (RegistrationNotFoundException ex) {
      log.debug("[X] Request to delete Registration with activityId={} resulted in RegistrationNotFound", activityId);
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND, ex.getMessage(), ex
      );
    }
  }
}
