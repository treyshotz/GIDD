package com.ntnu.gidd.controller.User;

import com.ntnu.gidd.dto.User.UserDto;
import com.ntnu.gidd.dto.User.UserRegistrationDto;
import com.ntnu.gidd.exception.EmailInUseException;
import com.ntnu.gidd.exception.UserNotFoundException;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.service.User.UserService;
import com.ntnu.gidd.util.Constants;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
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

    @GetMapping("{userId}/")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable UUID userId){
        try {
            return userService.getUserByUUID(userId);

        }catch (UserNotFoundException ex){
            log.error("[X] User with id {} not found, userId", userId);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<UserDto> getAllUser(@QuerydslPredicate(root = Activity.class) Predicate predicate,
                              @PageableDefault(size = Constants.PAGINATION_SIZE, sort="firstName", direction = Sort.Direction.ASC) Pageable pageable){
        return this.userService.getAll(predicate, pageable);
    }

    @GetMapping("me/")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(Authentication authentication){
        UserDetails user = (UserDetails) authentication.getPrincipal();
        return this.userService.getUserDtoByEmail(user.getUsername());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto){
        log.debug("[X] Request to save user with email={}", userRegistrationDto.getEmail());
        try{
            return userService.saveUser(userRegistrationDto);
        }
        catch (EmailInUseException exception){
            log.error("[X] Email is already in use", exception);
            throw new ResponseStatusException(
                  HttpStatus.FORBIDDEN, exception.getMessage(), exception);
        }
    }
}
