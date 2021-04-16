package com.ntnu.gidd.controller;

import com.ntnu.gidd.dto.UserDto;
import com.ntnu.gidd.dto.UserRegistrationDto;
import com.ntnu.gidd.exception.EmailInUseException;
import com.ntnu.gidd.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserServiceImpl userService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserDto createUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto){
		log.debug("[X] Request to save user with email={}", userRegistrationDto.getEmail());
		try{
			return userService.saveUser(userRegistrationDto);
		}
		catch (EmailInUseException exception){
			log.error(String.valueOf(exception));
			throw new ResponseStatusException(
					HttpStatus.FORBIDDEN, exception.getMessage(), exception);
		}
	}
}
