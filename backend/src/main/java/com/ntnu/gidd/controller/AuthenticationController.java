package com.ntnu.gidd.controller;

import com.ntnu.gidd.dto.JwtTokenResponse;
import com.ntnu.gidd.exception.RefreshTokenNotFound;
import com.ntnu.gidd.security.config.JWTConfig;
import com.ntnu.gidd.security.service.JwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("auth/")
@AllArgsConstructor
public class AuthenticationController {

    private JWTConfig jwtConfig;
    private JwtService jwtService;

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

}
