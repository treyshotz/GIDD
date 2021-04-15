package com.ntnu.gidd.controller;

import com.ntnu.gidd.model.Registration;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ntnu.gidd.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
  public Registration getRegistrationWithCompositeIdActivity(UUID userId, UUID activityId) {
    return registrationService.getRegistrationWithCompositeId(userId, activityId);
  }
}