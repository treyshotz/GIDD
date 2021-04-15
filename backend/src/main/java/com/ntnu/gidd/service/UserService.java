package com.ntnu.gidd.service;

import com.ntnu.gidd.dto.UserDto;
import com.ntnu.gidd.dto.UserRegistrationDto;
import com.ntnu.gidd.exception.EmailInUseException;
import com.ntnu.gidd.model.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UserService {
      UserDto updateUser(UUID id, User activity);
      UserDto getUserById(UUID id);
      UserDto saveUser(UserRegistrationDto user);
      UserDto deleteUser(UUID id);
}
