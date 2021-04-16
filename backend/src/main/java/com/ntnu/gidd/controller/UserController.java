package com.ntnu.gidd.controller;

import com.ntnu.gidd.dto.UserDto;
import com.ntnu.gidd.dto.UserRegistrationDto;
import com.ntnu.gidd.exception.EmailInUseException;
import com.ntnu.gidd.service.UserServiceImpl;
import com.ntnu.gidd.validation.ValidEmail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	UserServiceImpl userService;
	
	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@PostMapping
	public UserDto createUser(@RequestBody UserRegistrationDto userRegistrationDto){
		//TODO: Maybe this validation should made into a separate method?
		EmailValidator emailValidator = EmailValidator.getInstance();
		if(!emailValidator.isValid(userRegistrationDto.getEmail())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Email is not valid");
		}
		
		if(!userRegistrationDto.getPassword().equals(userRegistrationDto.getMatchingPassword())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Password does not match");
		}
		
		logger.debug("[X] Request to save user with email={}", userRegistrationDto.getEmail());
		try{
			return userService.saveUser(userRegistrationDto);
		}
		catch (EmailInUseException exception){
			logger.error(String.valueOf(exception));
			throw new ResponseStatusException(
					HttpStatus.FORBIDDEN, exception.getMessage(), exception);
		}
	}

	@GetMapping("/registration")
	public String showRegistrationForm(WebRequest request, Model model) {
		UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
		model.addAttribute("user", userRegistrationDto);
		return "registration";
	}
}
