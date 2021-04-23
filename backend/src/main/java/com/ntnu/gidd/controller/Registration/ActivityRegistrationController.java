package com.ntnu.gidd.controller.Registration;

import com.ntnu.gidd.dto.Registration.RegistrationUserDto;
import com.ntnu.gidd.dto.User.UserEmailDto;
import com.ntnu.gidd.exception.RegistrationNotFoundException;
import com.ntnu.gidd.model.Activity;
import java.util.UUID;

import com.ntnu.gidd.model.Registration;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ntnu.gidd.service.Registration.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("activities/{activityId}/registrations/")
public class ActivityRegistrationController {

  @Autowired
  private RegistrationService registrationService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public RegistrationUserDto postRegistration(@PathVariable UUID activityId, @RequestBody UserEmailDto user) {
    log.debug("[X] Request to Post Registration with userid={}", user.getEmail());
    return registrationService.saveRegistration(user.getId(), activityId);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Page<RegistrationUserDto> getRegistrationForActivity(@QuerydslPredicate(root = Registration.class) Predicate predicate,
                                                              @PageableDefault(size = Constants.PAGINATION_SIZE, sort="activity.startDate", direction = Sort.Direction.ASC) Pageable pageable,
                                                              @PathVariable UUID activityId) {
    log.debug("[X] Request to look up user registered for activity with id={}", activityId);
    return registrationService.getRegistrationForActivity(predicate, pageable, activityId);
  }

  @GetMapping("{userId}/")
  @ResponseStatus(HttpStatus.OK)
  public RegistrationUserDto getRegistrationWithCompositeIdActivity(@PathVariable UUID userId, @PathVariable UUID activityId) {
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
