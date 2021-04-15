package com.ntnu.gidd.service;

import com.ntnu.gidd.model.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UserService {
      User updateUser(UUID id, User activity);
      User getUserById(UUID id);
      User saveUser(User activity);
      User deleteUser(UUID id);
}
