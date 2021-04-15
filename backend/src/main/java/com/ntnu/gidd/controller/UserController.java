package com.ntnu.gidd.controller;

import com.ntnu.gidd.dto.UserDto;
import com.ntnu.gidd.dto.UserRegistrationDto;
import com.ntnu.gidd.exception.EmailInUseException;
import com.ntnu.gidd.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("users/")
public class UserController {
	
	@Autowired
	UserService userService;
	
	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@PostMapping
	public UserDto createUser(@ModelAttribute("user") @Valid UserRegistrationDto userRegistrationDto){
		logger.debug("[X] Request to save user with email={}", userRegistrationDto.getEmail());
		try{
			return userService.saveUser(userRegistrationDto);
		}
		catch (EmailInUseException exception){
			logger.error(String.valueOf(exception));
			return null;
		}
	}

	@GetMapping("/user/registration")
	public String showRegistrationForm(WebRequest request, Model model) {
		UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
		model.addAttribute("user", userRegistrationDto);
		return "registration";
	}
}
