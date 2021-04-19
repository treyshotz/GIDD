package com.ntnu.gidd.service;

import com.ntnu.gidd.dto.UserUpdateDto;
import org.springframework.stereotype.Service;
import java.util.UUID;
import com.ntnu.gidd.dto.UserDto;
import com.ntnu.gidd.dto.UserRegistrationDto;

@Service
public interface UserService {
      UserUpdateDto updateUser(UUID id, UserUpdateDto user);
      UserDto getUserById(UUID id);
      UserDto saveUser(UserRegistrationDto user);
      UserDto deleteUser(UUID id);
}
