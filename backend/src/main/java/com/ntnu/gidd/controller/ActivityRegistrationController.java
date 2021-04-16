package com.ntnu.gidd.controller;

import com.ntnu.gidd.exception.RegistrationNotFoundException;
import com.ntnu.gidd.model.Registration;
import java.util.List;
import java.util.UUID;

import com.ntnu.gidd.model.RegistrationId;
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
@RequestMapping("activities/{activityId}/registrations/")
public class ActivityRegistrationController {

  @Autowired
  private RegistrationService registrationService;

  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  public Registration postRegistration(@RequestBody Registration registration) {
    log.debug("[X] Request to Post Registration with id={}", registration.getRegistrationId());
    return registrationService.saveRegistration(registration);
  }

  @GetMapping("")
  @ResponseStatus(HttpStatus.OK)
  public List<Registration> getRegistrationForActivity(@PathVariable UUID activityId) {
    log.debug("[X] Request to look up user registered for activity with id={}", activityId);
    return registrationService.getRegistrationForActivity(activityId);
  }

  @GetMapping("{userId}/")
  @ResponseStatus(HttpStatus.OK)
  public Registration getRegistrationWithCompositeIdActivity(@PathVariable UUID userId, @PathVariable UUID activityId) {
    return registrationService.getRegistrationWithCompositeId(userId, activityId);
  }

  @DeleteMapping("{userId}/")
  @ResponseStatus(HttpStatus.OK)
  public Response deleteRegistration(@PathVariable UUID activityId, UUID userId){
    try {
      log.debug("[X] Request to delete Registration with userId={} ", userId);
      registrationService.deleteRegistrationWithCompositeId(userId, activityId);
      return new Response("Registration has been deleted");
    } catch (RegistrationNotFoundException ex) {
      log.debug("[X] Request to delete Registration with userId={} resulted in RegistrationNotFound", userId);
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND, ex.getMessage(), ex
      );
    }
  }

}
