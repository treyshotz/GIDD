package com.ntnu.gidd.service.Registration;

import com.ntnu.gidd.dto.Registration.RegistrationActivityDto;
import com.ntnu.gidd.dto.Registration.RegistrationActivityListDto;
import com.ntnu.gidd.dto.Registration.RegistrationUserDto;
import com.ntnu.gidd.exception.ActivityNotFoundExecption;
import com.ntnu.gidd.exception.RegistrationNotFoundException;
import com.ntnu.gidd.exception.UserNotFoundException;
import com.ntnu.gidd.model.*;
import com.ntnu.gidd.repository.ActivityRepository;
import com.ntnu.gidd.repository.RegistrationRepository;

import java.util.UUID;

import com.ntnu.gidd.repository.UserRepository;
import com.ntnu.gidd.service.Registration.RegistrationService;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * RegistrationServiceImpl class for services for the RegistrationRepository
 */
@Service
public class RegistrationServiceImpl implements RegistrationService {

  @Autowired
  RegistrationRepository registrationRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  ActivityRepository activityRepository;

  ModelMapper modelMapper = new ModelMapper();

  @Override
  public RegistrationUserDto saveRegistration(UUID user_id, UUID activity_id){
    User user = userRepository.findById(user_id).orElseThrow(UserNotFoundException::new);
    Activity activity = activityRepository.findById(activity_id).orElseThrow(ActivityNotFoundExecption::new);
    Registration registration = registrationRepository.save(new Registration(new RegistrationId(user_id, activity_id), user, activity));
    return modelMapper.map(registration, RegistrationUserDto.class);
  }

  /**
   * Finds all registration for a given activity
   *
   * @param predicate
   * @param pageable
   * @param activity_id
   * @return Page of registration or throws Exception
   */
  @Override
  public Page<RegistrationUserDto> getRegistrationForActivity(Predicate predicate, Pageable pageable, UUID activity_id){
    QRegistration registration = QRegistration.registration;
      predicate = ExpressionUtils.allOf(predicate, registration.activity.id.eq(activity_id));
      Page<Registration> registrations = registrationRepository.findAll(predicate, pageable);
      return registrations.map(p -> modelMapper.map(p, RegistrationUserDto.class));
  }

  /**
   * Finds all registration for a given user by its email
   *
   * @param predicate
   * @param pageable
   * @param username
   * @return Page of registration or throws Exception
   */

  @Override
  public Page<RegistrationActivityListDto> getRegistrationWithUsername(Predicate predicate, Pageable pageable, String username) {
    User user = userRepository.findByEmail(username)
            .orElseThrow(UserNotFoundException::new);

    QRegistration registration = QRegistration.registration;
    predicate = ExpressionUtils.allOf(registration.user.id.eq(user.getId()).and(predicate));

    Page<Registration> registrations = registrationRepository.findAll(predicate, pageable);
    return registrations.map(p -> modelMapper.map(p, RegistrationActivityListDto.class));
  }

  /**
   * Finds registration with composite id
   * @param user_id
   * @param activity_id
   * @return registration or throws exception
   */
  @Override
  public RegistrationUserDto getRegistrationWithCompositeId(UUID user_id, UUID activity_id) {
    Registration registration = registrationRepository.findRegistrationByUser_IdAndActivity_Id(user_id, activity_id)
            .orElseThrow(RegistrationNotFoundException::new);
    return modelMapper.map(registration, RegistrationUserDto.class);
  }

  @Override
  public RegistrationActivityDto getRegistrationWithUsernameAndActivityId(String username, UUID activity_id) {
    User user = userRepository.findByEmail(username)
            .orElseThrow(UserNotFoundException::new);
    Registration registration = registrationRepository.findRegistrationByUser_IdAndActivity_Id(user.getId(), activity_id)
            .orElseThrow(RegistrationNotFoundException::new);
    return modelMapper.map(registration, RegistrationActivityDto.class);
  }

  /**
   * Find the registration with the corresponding registration id
   * @param id
   * @return Registration or throws exception
   */
  @Override
  public RegistrationUserDto getRegistrationWithRegistrationId(RegistrationId id) {
    Registration registration = registrationRepository.findById(id).
        orElseThrow(RegistrationNotFoundException::new);
    return modelMapper.map(registration, RegistrationUserDto.class);
  }

  /**
   * Find the registration with the registration id
   * Deletes the registration
   * @param user_id, activity_id
   * @return deletion or throws exception
   */
  @Override
  @Transactional
  public void deleteRegistrationWithCompositeId(UUID user_id, UUID activity_id) {
    registrationRepository.findRegistrationByUser_IdAndActivity_Id(user_id, activity_id)
            .orElseThrow(RegistrationNotFoundException::new);
    registrationRepository.deleteRegistrationsByUser_IdAndActivity_Id(user_id, activity_id);
  }

  /**
   * Find the registration with the users id and the activity id
   *  Deletes the registration
   * @param username, activity_id
   * @return deletion or throws exception
   */

  @Override
  public void deleteRegistrationWithUsernameAndActivityId(String username, UUID activity_id) {
    User user = userRepository.findByEmail(username).
            orElseThrow(UserNotFoundException::new);
    deleteRegistrationWithCompositeId(user.getId(), activity_id);
  }
}
