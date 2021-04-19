package com.ntnu.gidd.service;

import com.ntnu.gidd.dto.UserDto;
import com.ntnu.gidd.dto.UserPasswordUpdateDto;
import com.ntnu.gidd.dto.UserRegistrationDto;
import com.ntnu.gidd.exception.EmailInUseException;
import com.ntnu.gidd.exception.UserNotFoundException;
import com.ntnu.gidd.model.TrainingLevel;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.TrainingLevelRepository;
import com.ntnu.gidd.repository.UserRepository;
import com.ntnu.gidd.util.TrainingLevelEnum;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Slf4j
@NoArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	private ModelMapper modelMapper = new ModelMapper();
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDto getUserById(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		return modelMapper.map(user, UserDto.class);
	}
	@Override
		Optional<User> user = userRepository.findByEmail(email);
	public UserDto getUserByEmail(String email) {
		return user.map(value -> modelMapper.map(value, UserDto.class)).orElse(null);
	}
	private boolean emailExist(String email) {
		return userRepository.findByEmail(email).isPresent();
	}
	
	@Override
	public UserDto saveUser(UserRegistrationDto user) {
		if (emailExist(user.getEmail())) {
			throw new EmailInUseException();
		}
		
		User userObj = modelMapper.map(user, User.class);
		userObj.setPassword(passwordEncoder.encode(user.getPassword()));
		return modelMapper.map(userRepository.save(userObj), UserDto.class);
	}
	
	@Override
	public void changePassword(Principal principal, UserPasswordUpdateDto user) {
		User userObj = userRepository.findByEmail(principal.getName()).get();
		if (passwordEncoder.matches(user.getOldPassword(), userObj.getPassword())) {
			if (user.getNewPassword().equals(user.getNewPasswordConfirmed())) {
				userObj.setPassword(user.getNewPassword());
				userRepository.save(userObj);
				userRepository.flush();
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Passwords does not match");
			}
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Password is not correct");
		}
	}
	
	@Override
	public UserDto deleteUser(UUID id) {
		return null;
	}

	@Autowired
	private TrainingLevelRepository trainingLevelRepository;

	public UserDto updateUser(UUID id, UserDto user){
		User updatedUser = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
		updatedUser.setFirstName(user.getFirstName());
		updatedUser.setSurname(user.getSurname());
		updatedUser.setEmail(user.getEmail());
		updatedUser.setBirthDate(user.getBirthDate());
		if(user.getLevel()!=null)updatedUser.setTrainingLevel(getTrainingLevel(user.getLevel()));
		return modelMapper.map(userRepository.save(updatedUser), UserDto.class);
	}

	private TrainingLevel getTrainingLevel(TrainingLevelEnum level){
		return trainingLevelRepository.findTrainingLevelByLevel(level).
				orElseThrow(() -> new EntityNotFoundException("Traning level does not exist"));
	}
}
