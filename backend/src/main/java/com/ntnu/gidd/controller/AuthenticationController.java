package com.ntnu.gidd.controller;

import com.ntnu.gidd.security.config.JWTConfig;
import com.ntnu.gidd.security.service.JwtService;
import com.ntnu.gidd.security.token.JwtToken;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("auth/")
@AllArgsConstructor
public class AuthenticationController {

    private JWTConfig jwtConfig;
    private JwtService jwtService;

    @GetMapping("/refresh-token")
    public JwtToken refreshToken(HttpServletRequest request) {
        String header = request.getHeader(jwtConfig.getHeader());
        return jwtService.refreshToken(header);
    }

}
