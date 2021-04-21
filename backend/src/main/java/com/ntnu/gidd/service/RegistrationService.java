package com.ntnu.gidd.service;

import com.ntnu.gidd.dto.RegistrationDto;

import java.util.UUID;

import com.ntnu.gidd.model.RegistrationId;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface RegistrationService {

  RegistrationDto saveRegistration(UUID user_id, UUID activity_id);

  Page<RegistrationDto> getRegistrationForActivity(Predicate predicate, Pageable pageable, UUID activity_id);

  Page<RegistrationDto> getRegistrationWithUsername(Predicate predicate, Pageable pageable, String username);

  RegistrationDto getRegistrationWithRegistrationId(RegistrationId id);

  RegistrationDto getRegistrationWithCompositeId(UUID user_id, UUID activity_id);

  RegistrationDto getRegistrationWithUsernameAndActivityId(String username, UUID activity_id);

  void deleteRegistrationWithCompositeId(UUID user_id, UUID activity_id);

  void deleteRegistrationWithUsernameAndActivityId(String username, UUID activity_id);
}
