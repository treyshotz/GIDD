package com.ntnu.gidd.service;

import com.ntnu.gidd.dto.UserDto;
import com.ntnu.gidd.dto.UserPasswordUpdateDto;
import com.ntnu.gidd.dto.UserRegistrationDto;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.UUID;

@Service
public interface UserService {
      UserDto updateUser(UUID id, UserDto user);
      UserDto getUserById(String email);
      UserDto saveUser(UserRegistrationDto user);
      UserDto deleteUser(UUID id);
      void changePassword(Principal principal, UserPasswordUpdateDto user);
}
