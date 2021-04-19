package com.ntnu.gidd.controller.User;

import com.ntnu.gidd.dto.UserDto;
import com.ntnu.gidd.dto.UserRegistrationDto;
import com.ntnu.gidd.dto.UserUpdateDto;
import com.ntnu.gidd.exception.EmailInUseException;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.service.UserDetailsServiceImpl;
import com.ntnu.gidd.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @PutMapping("{userId}/")
    @ResponseStatus(HttpStatus.OK)
    public User updateActivity(@PathVariable UUID userId, @RequestBody UserUpdateDto user){
        return this.userDetailsServiceImpl.updateUser(userId, user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto){
        log.debug("[X] Request to save user with email={}", userRegistrationDto.getEmail());
        try{
            return userService.saveUser(userRegistrationDto);
        }
        catch (EmailInUseException exception){
            log.error("Email is already in use", exception);
            throw new ResponseStatusException(
                  HttpStatus.FORBIDDEN, exception.getMessage(), exception);
        }
    }
}
