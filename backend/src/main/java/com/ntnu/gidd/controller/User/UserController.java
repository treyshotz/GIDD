package com.ntnu.gidd.controller.User;

import com.ntnu.gidd.dto.UserDto;
import com.ntnu.gidd.dto.UserPasswordUpdateDto;
import com.ntnu.gidd.dto.UserRegistrationDto;
import com.ntnu.gidd.exception.EmailInUseException;
import com.ntnu.gidd.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/users/")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("{userId}/")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@PathVariable UUID userId, @RequestBody UserDto user){
        return this.userService.updateUser(userId, user);
    }

    @GetMapping("me/")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(Authentication authentication){
        UserDetails user = (UserDetails) authentication.getPrincipal();
        return this.userService.getUserById(user.getUsername());
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
    
    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassword(Principal principal, @RequestBody UserPasswordUpdateDto user) {
        userService.changePassword(principal, user);
    }
}
