package com.ntnu.gidd.service;

import com.ntnu.gidd.exception.RegistrationNotFoundException;
import com.ntnu.gidd.model.Registration;
import com.ntnu.gidd.model.RegistrationId;
import com.ntnu.gidd.repository.RegistrationRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RegistrationServiceImpl class for services for the RegistrationRepository
 */
@Service
public class RegistrationServiceImpl implements RegistrationService {

  @Autowired
  RegistrationRepository registrationRepository;

  @Override
  public Registration saveRegistration(Registration registration){
    return registrationRepository.save(registration);
  }

  /**
   * Finds all registration for a given activity
   * @param activity_id
   * @return List of registration or throws Exception
   */
  @Override
  public List<Registration> getRegistrationForActivity(UUID activity_id){
      return registrationRepository.findRegistrationsByActivity_Id(activity_id)
              .orElseThrow(RegistrationNotFoundException::new);
  }

  /**
   * Finds all registration for a given user
   * @param user_id
   * @return List of registration or throws exception
   */
  @Override
  public List<Registration> getRegistrationsForUser(UUID user_id) {
    return registrationRepository.findRegistrationsByUser_Id(user_id)
            .orElseThrow(RegistrationNotFoundException::new);
  }

  /**
   * Finds registration with composite id
   * @param user_id
   * @param activity_id
   * @return registration or throws exception
   */
  @Override
  public Registration getRegistrationWithCompositeId(UUID user_id, UUID activity_id) {
    return registrationRepository.findRegistrationByUser_IdAndActivity_Id(user_id, activity_id)
            .orElseThrow(RegistrationNotFoundException::new);
  }

  /**
   * Find the registration with the corresponding registration id
   * @param id
   * @return Registration or throws exception
   */
  @Override
  public Registration getRegistrationWithRegistrationId(RegistrationId id) {
    return registrationRepository.findById(id).
        orElseThrow(RegistrationNotFoundException::new);
  }

  /**
   * Find the registration with the registration id
   * Deletes the registration
   * @param user_id, activity_id
   * @return deletion or throws exception
   */
  @Override
  public void deleteRegistrationWithCompositeId(UUID user_id, UUID activity_id) {
    registrationRepository.findRegistrationByUser_IdAndActivity_Id(user_id, activity_id)
            .orElseThrow(RegistrationNotFoundException::new);
    registrationRepository.deleteRegistrationsByUser_IdAndActivity_Id(user_id, activity_id);
  }
}
