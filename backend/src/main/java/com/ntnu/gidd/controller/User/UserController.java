package com.ntnu.gidd.controller.User;

import com.ntnu.gidd.dto.UserUpdateDto;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("users/")
public class UserController {


    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @PutMapping("{userId}/")
    @ResponseStatus(HttpStatus.OK)
    public User updateActivity(@PathVariable UUID userId, @RequestBody UserUpdateDto user){
        return this.userDetailsServiceImpl.updateUser(userId, user);
    }
}
