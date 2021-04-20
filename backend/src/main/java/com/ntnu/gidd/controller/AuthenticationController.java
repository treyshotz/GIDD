package com.ntnu.gidd.controller;

import com.ntnu.gidd.dto.UserPasswordUpdateDto;
import com.ntnu.gidd.dto.JwtTokenResponse;
import com.ntnu.gidd.exception.PasswordIsIncorrectException;
import com.ntnu.gidd.exception.RefreshTokenNotFound;
import com.ntnu.gidd.security.config.JWTConfig;
import com.ntnu.gidd.security.service.JwtService;
import com.ntnu.gidd.security.token.JwtToken;
import com.ntnu.gidd.service.UserService;
import com.ntnu.gidd.util.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("auth/")
@AllArgsConstructor
public class AuthenticationController {

    private JWTConfig jwtConfig;
    private JwtService jwtService;
    
    @Autowired
    private UserService userService;

    @GetMapping("/refresh-token/")
    public JwtTokenResponse refreshToken(HttpServletRequest request) {
        String header = request.getHeader(jwtConfig.getHeader());
        try {
            return jwtService.refreshToken(header);
        } catch (RefreshTokenNotFound ex) {
            log.error("[X] Token refresh failed with exception", ex);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
    }
    
    @PostMapping("/change-password/")
    @ResponseStatus(HttpStatus.OK)
    public Response updatePassword(Principal principal, @RequestBody UserPasswordUpdateDto user) {
        try {
            userService.changePassword(principal, user);
        } catch (PasswordIsIncorrectException ex) {
            log.error("[X] User {} tried to change password with incorrect current password", principal.getName() , ex);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, ex.getMessage());
        }
        return new Response("Password was successfully changed");
    }
}
