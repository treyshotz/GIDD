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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("@securityService.isUser(#userId)")
    public UserDto updateUser(@PathVariable UUID userId, @RequestBody UserDto user){
        log.debug("[X] Request to update user with id={}", user.getId());
        return this.userService.updateUser(userId, user);
    }

    @GetMapping("{userId}/")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable UUID userId){
        log.debug("[X] Request to get user with id={}", userId);
        return userService.getUserByUUID(userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<UserDto> getAllUser(@QuerydslPredicate(root = Activity.class) Predicate predicate,
                              @PageableDefault(size = Constants.PAGINATION_SIZE, sort="firstName", direction = Sort.Direction.ASC) Pageable pageable){
        log.debug("[X] Request to look up users");
        return this.userService.getAll(predicate, pageable);
    }

    @GetMapping("me/")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(Authentication authentication){
        UserDetails user = (UserDetails) authentication.getPrincipal();

        log.debug("[X] Request to get personal userinfo with token");
        return this.userService.getUserDtoByEmail(user.getUsername());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto){
        log.debug("[X] Request to save user with email={}", userRegistrationDto.getEmail());
        return userService.saveUser(userRegistrationDto);
    }
}
