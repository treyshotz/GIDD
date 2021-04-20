package com.ntnu.gidd.controller;

import com.ntnu.gidd.dto.UserPasswordUpdateDto;
import com.ntnu.gidd.security.config.JWTConfig;
import com.ntnu.gidd.security.service.JwtService;
import com.ntnu.gidd.security.token.JwtToken;
import com.ntnu.gidd.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("auth/")
@AllArgsConstructor
public class AuthenticationController {

    private JWTConfig jwtConfig;
    private JwtService jwtService;
    
    @Autowired
    private UserService userService;

    @GetMapping("/refresh-token")
    public JwtToken refreshToken(HttpServletRequest request) {
        String header = request.getHeader(jwtConfig.getHeader());
        return jwtService.refreshToken(header);
    }
    
    @PostMapping("/change-password/")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassword(Principal principal, @RequestBody UserPasswordUpdateDto user) {
        userService.changePassword(principal, user);
    }
}
