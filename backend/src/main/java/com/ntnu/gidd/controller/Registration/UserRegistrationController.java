package com.ntnu.gidd.controller.Registration;

import com.ntnu.gidd.dto.RegistrationDto;
import com.ntnu.gidd.exception.RegistrationNotFoundException;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.Registration;
import java.util.List;
import java.util.UUID;

import com.ntnu.gidd.service.RegistrationService;
import com.ntnu.gidd.util.Constants;
import com.ntnu.gidd.util.Response;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
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
  public Page<RegistrationDto> getRegistrationsForUser(@QuerydslPredicate(root = Activity.class) Predicate predicate,
                                                       @PageableDefault(size = Constants.PAGINATION_SIZE,
                                                               sort="activity.startDate", direction = Sort.Direction.ASC) Pageable pageable,
                                                       Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    log.debug("[X] Request to look up acivites registered for user with username={}", userDetails.getUsername());
    return registrationService.getRegistrationWithUsername(predicate, pageable, userDetails.getUsername());
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
