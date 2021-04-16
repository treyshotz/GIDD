package com.ntnu.gidd.service;

import com.ntnu.gidd.model.Registration;
import java.util.List;
import java.util.UUID;

import com.ntnu.gidd.model.RegistrationId;
import org.springframework.stereotype.Service;

@Service
public interface RegistrationService {

  Registration saveRegistration(Registration registration);

  List<Registration> getRegistrationForActivity(UUID activity_id);

  List<Registration> getRegistrationsForUser(UUID user_id);

  Registration getRegistrationWithRegistrationId(RegistrationId id);

  Registration getRegistrationWithCompositeId(UUID user_id, UUID activity_id);

  void deleteRegistrationWithCompositeId(UUID user_id, UUID activity_id);
}
