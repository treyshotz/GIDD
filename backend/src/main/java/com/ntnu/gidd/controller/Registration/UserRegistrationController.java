package com.ntnu.gidd.controller.Registration;

import com.ntnu.gidd.dto.RegistrationDto;
import com.ntnu.gidd.exception.RegistrationNotFoundException;
import com.ntnu.gidd.service.RegistrationService;
import com.ntnu.gidd.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("users/registrations/")
public class UserRegistrationController {

  @Autowired
  private RegistrationService registrationService;

  @GetMapping("")
  @ResponseStatus(HttpStatus.OK)
  public List<RegistrationDto> getRegistrationsForUser(Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    log.debug("[X] Request to look up acivites registered for user with username={}", userDetails.getUsername());
    return registrationService.getRegistrationWithUsername(userDetails.getUsername());
  }

  @GetMapping("{activityId}/")
  @ResponseStatus(HttpStatus.OK)
  public RegistrationDto getRegistrationWithCompositeIdUser(Authentication authentication, @PathVariable UUID activityId) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return registrationService.getRegistrationWithUsernameAndActivityId(userDetails.getUsername(), activityId);
  }

  @DeleteMapping("{activityId}/")
  @ResponseStatus(HttpStatus.OK)
  public Response deleteRegistration(Authentication authentication, @PathVariable UUID activityId){
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    try {
      log.debug("[X] Request to delete Registration with activityId={} ", activityId);
      registrationService.deleteRegistrationWithUsernameAndActivityId(userDetails.getUsername(), activityId);
      return new Response("Registration has been deleted");
    } catch (RegistrationNotFoundException ex) {
      log.debug("[X] Request to delete Registration with activityId={} resulted in RegistrationNotFound", activityId);
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND, ex.getMessage(), ex
      );
    }
  }
}
