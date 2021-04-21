package com.ntnu.gidd.controller.Registration;

import com.ntnu.gidd.dto.RegistrationDto;
import com.ntnu.gidd.dto.UserEmailDto;
import com.ntnu.gidd.exception.RegistrationNotFoundException;
import com.ntnu.gidd.service.RegistrationService;
import com.ntnu.gidd.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("activities/{activityId}/registrations/")
public class ActivityRegistrationController {

  @Autowired
  private RegistrationService registrationService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public RegistrationDto postRegistration(@PathVariable UUID activityId, @RequestBody UserEmailDto user) {
    log.debug("[X] Request to Post Registration with userid={}", user.getEmail());
    return registrationService.saveRegistration(user.getId(), activityId);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<RegistrationDto> getRegistrationForActivity(@PathVariable UUID activityId) {
    log.debug("[X] Request to look up user registered for activity with id={}", activityId);
    return registrationService.getRegistrationForActivity(activityId);
  }

  @GetMapping("{userId}/")
  @ResponseStatus(HttpStatus.OK)
  public RegistrationDto getRegistrationWithCompositeIdActivity(@PathVariable UUID userId, @PathVariable UUID activityId) {
    return registrationService.getRegistrationWithCompositeId(userId, activityId);
  }

  @DeleteMapping("{userId}/")
  @ResponseStatus(HttpStatus.OK)
  public Response deleteRegistration(@PathVariable UUID activityId, @PathVariable UUID userId){
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
