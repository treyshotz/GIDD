package com.ntnu.gidd.service;

import com.ntnu.gidd.dto.RegistrationDto;
import java.util.List;
import java.util.UUID;

import com.ntnu.gidd.model.RegistrationId;
import org.springframework.stereotype.Service;

@Service
public interface RegistrationService {

  RegistrationDto saveRegistration(UUID user_id, UUID activity_id);

  List<RegistrationDto> getRegistrationForActivity(UUID activity_id);

  List<RegistrationDto> getRegistrationsForUser(UUID user_id);

  List<RegistrationDto> getRegistrationWithUsername(String username);

  RegistrationDto getRegistrationWithRegistrationId(RegistrationId id);

  RegistrationDto getRegistrationWithCompositeId(UUID user_id, UUID activity_id);

  RegistrationDto getRegistrationWithUsernameAndActivityId(String username, UUID activity_id);

  void deleteRegistrationWithCompositeId(UUID user_id, UUID activity_id);

  void deleteRegistrationWithUsernameAndActivityId(String username, UUID activity_id);
}
