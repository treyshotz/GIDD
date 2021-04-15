package com.ntnu.gidd.controller;

import com.ntnu.gidd.dto.UserRegistrationDto;
import com.ntnu.gidd.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("users/")
public class UserController {
	
	@Autowired
	UserService userService;
	
	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@PostMapping
	public UserRegistrationDto createUser(@RequestBody UserRegistrationDto userDto){
		logger.debug("[X] Request to save user with email={}", userDto.getEmail());
		return userService.saveUser(userDto);
	}
}
