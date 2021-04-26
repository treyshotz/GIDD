package com.ntnu.gidd.service.User;

import com.ntnu.gidd.dto.User.UserDto;
import com.ntnu.gidd.dto.User.UserPasswordResetDto;
import com.ntnu.gidd.dto.User.UserPasswordUpdateDto;
import com.ntnu.gidd.dto.User.UserRegistrationDto;
import com.ntnu.gidd.model.PasswordResetToken;
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
      UUID forgotPassword(String email);
      void validateResetPassword(UserPasswordResetDto user, UUID passwordResetTokenId);
}
