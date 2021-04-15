package com.ntnu.gidd.service;

import com.ntnu.gidd.exception.RegistrationNotFoundException;
import com.ntnu.gidd.model.Registration;
import com.ntnu.gidd.model.RegistrationId;
import com.ntnu.gidd.repository.RegistrationRepository;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {

  @Autowired
  RegistrationRepository registrationRepository;

  @Override
  public Registration saveRegistration(Registration registration){
    return registrationRepository.save(registration);
  }

  @Override
  public List<Registration> getRegistrationForActivity(UUID activity_id){
      return registrationRepository.findRegistrationsByActivity_Id(activity_id)
              .orElseThrow(RegistrationNotFoundException::new);
  }

  @Override
  public List<Registration> getRegistrationsForUser(UUID user_id) {
    return registrationRepository.findRegistrationsByUser_Id(user_id)
            .orElseThrow(RegistrationNotFoundException::new);
  }

  @Override
  public Registration getRegistrationWithCompositeId(UUID user_id, UUID activity_id) {
    return registrationRepository.findRegistrationByUser_IdAndActivity_Id(user_id, activity_id)
            .orElseThrow(RegistrationNotFoundException::new);
  }

  @Override
  public Registration getRegistrationWithRegistrationId(RegistrationId id) {
    return registrationRepository.findById(id).
        orElseThrow(RegistrationNotFoundException::new);
  }








}
