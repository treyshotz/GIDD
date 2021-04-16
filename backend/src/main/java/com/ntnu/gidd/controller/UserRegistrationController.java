package com.ntnu.gidd.controller;

import com.ntnu.gidd.model.Registration;
import java.util.List;
import java.util.UUID;
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
}
