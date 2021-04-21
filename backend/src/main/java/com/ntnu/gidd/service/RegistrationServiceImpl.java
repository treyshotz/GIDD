package com.ntnu.gidd.service;

import com.ntnu.gidd.dto.RegistrationDto;
import com.ntnu.gidd.exception.ActivityNotFoundExecption;
import com.ntnu.gidd.exception.RegistrationNotFoundException;
import com.ntnu.gidd.exception.UserNotFoundException;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.Registration;
import com.ntnu.gidd.model.RegistrationId;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.ActivityRepository;
import com.ntnu.gidd.repository.RegistrationRepository;
import com.ntnu.gidd.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
  public RegistrationDto saveRegistration(UUID user_id, UUID activity_id){
    User user = userRepository.findById(user_id).orElseThrow(UserNotFoundException::new);
    Activity activity = activityRepository.findById(activity_id).orElseThrow(ActivityNotFoundExecption::new);
    Registration registration = registrationRepository.save(new Registration(new RegistrationId(user_id, activity_id), user, activity));
    return modelMapper.map(registration, RegistrationDto.class);
  }

  /**
   * Finds all registration for a given activity
   * @param activity_id
   * @return List of registration or throws Exception
   */
  @Override
  public List<RegistrationDto> getRegistrationForActivity(UUID activity_id){
      List<Registration> registrations = (registrationRepository.findRegistrationsByActivity_Id(activity_id)
              .orElseThrow(RegistrationNotFoundException::new));
      return registrations.stream().map(p -> modelMapper.map(p, RegistrationDto.class)).collect(Collectors.toList());
  }

  /**
   * Finds all registration for a given user
   * @param user_id
   * @return List of registration or throws exception
   */
  @Override
  public List<RegistrationDto> getRegistrationsForUser(UUID user_id) {
    List<Registration> registrations = registrationRepository.findRegistrationsByUser_Id(user_id)
            .orElseThrow(RegistrationNotFoundException::new);
    return registrations.stream().map(p -> modelMapper.map(p, RegistrationDto.class)).collect(Collectors.toList());
  }

  @Override
  public List<RegistrationDto> getRegistrationWithUsername(String username) {
    User user = userRepository.findByEmail(username)
            .orElseThrow(UserNotFoundException::new);
    List<Registration> registrations = registrationRepository.findRegistrationsByUser_Id(user.getId())
            .orElseThrow(RegistrationNotFoundException::new);
    return registrations.stream().map(p -> modelMapper.map(p, RegistrationDto.class)).collect(Collectors.toList());
  }

  /**
   * Finds registration with composite id
   * @param user_id
   * @param activity_id
   * @return registration or throws exception
   */
  @Override
  public RegistrationDto getRegistrationWithCompositeId(UUID user_id, UUID activity_id) {
    Registration registration = registrationRepository.findRegistrationByUser_IdAndActivity_Id(user_id, activity_id)
            .orElseThrow(RegistrationNotFoundException::new);
    return modelMapper.map(registration, RegistrationDto.class);
  }

  @Override
  public RegistrationDto getRegistrationWithUsernameAndActivityId(String username, UUID activity_id) {
    User user = userRepository.findByEmail(username)
            .orElseThrow(UserNotFoundException::new);
    Registration registration = registrationRepository.findRegistrationByUser_IdAndActivity_Id(user.getId(), activity_id)
            .orElseThrow(RegistrationNotFoundException::new);
    return modelMapper.map(registration, RegistrationDto.class);
  }

  /**
   * Find the registration with the corresponding registration id
   * @param id
   * @return Registration or throws exception
   */
  @Override
  public RegistrationDto getRegistrationWithRegistrationId(RegistrationId id) {
    Registration registration = registrationRepository.findById(id).
        orElseThrow(RegistrationNotFoundException::new);
    return modelMapper.map(registration, RegistrationDto.class);
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

  @Override
  public void deleteRegistrationWithUsernameAndActivityId(String username, UUID activity_id) {
    User user = userRepository.findByEmail(username).
            orElseThrow(UserNotFoundException::new);
    deleteRegistrationWithCompositeId(user.getId(), activity_id);
  }
}
