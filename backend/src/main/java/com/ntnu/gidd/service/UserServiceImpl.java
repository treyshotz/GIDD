package com.ntnu.gidd.service;

import com.ntnu.gidd.dto.UserDto;
import com.ntnu.gidd.dto.UserRegistrationDto;
import com.ntnu.gidd.exception.EmailInUseException;
import com.ntnu.gidd.exception.UserNotFoundException;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Slf4j
@NoArgsConstructor
public class UserServiceImpl implements UserService {
	ModelMapper modelMapper = new ModelMapper();
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDto updateUser(UUID id, User activity) {
		return null;
	}
	
	@Override
	public UserDto getUserById(UUID id) {
		return null;
	}
	
	public UserDto getUserByEmail(String email) {
		return modelMapper.map(this.userRepository.findByEmail(email)
						.orElseThrow(() -> new UserNotFoundException("This activity does not exist")), UserDto.class);
	}
	
	@Override
	public UserDto saveUser(UserRegistrationDto user) {
		//TODO: Verify that all the credentials are correct and that the user information doesn't exist
		// Hash the password and salt
		if(userRepository.findByEmail(user.getEmail()).isPresent()){
			throw new EmailInUseException();
		}

		User userObj = modelMapper.map(user, User.class);
		return modelMapper.map(userRepository.save(userObj), UserDto.class);
	}
	
	@Override
	public UserDto deleteUser(UUID id) {
		return null;
	}
}
