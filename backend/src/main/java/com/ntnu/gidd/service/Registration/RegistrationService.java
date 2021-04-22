package com.ntnu.gidd.service.Registration;

import com.ntnu.gidd.dto.Registration.RegistrationActivityDto;
import com.ntnu.gidd.dto.Registration.RegistrationActivityListDto;
import com.ntnu.gidd.dto.Registration.RegistrationUserDto;

import java.util.UUID;

import com.ntnu.gidd.model.RegistrationId;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface RegistrationService {

  RegistrationUserDto saveRegistration(UUID user_id, UUID activity_id);

  Page<RegistrationUserDto> getRegistrationForActivity(Predicate predicate, Pageable pageable, UUID activity_id);

  Page<RegistrationActivityListDto> getRegistrationWithUsername(Predicate predicate, Pageable pageable, String username);

  RegistrationUserDto getRegistrationWithRegistrationId(RegistrationId id);

  RegistrationUserDto getRegistrationWithCompositeId(UUID user_id, UUID activity_id);

  RegistrationActivityDto getRegistrationWithUsernameAndActivityId(String username, UUID activity_id);

  void deleteRegistrationWithCompositeId(UUID user_id, UUID activity_id);

  void deleteRegistrationWithUsernameAndActivityId(String username, UUID activity_id);
}
