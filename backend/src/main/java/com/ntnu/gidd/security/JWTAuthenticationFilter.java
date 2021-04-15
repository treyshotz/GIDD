package com.ntnu.gidd.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntnu.gidd.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


/**
 * Filter to check the existence and validity of the access token on the Authorization header.
 */
@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private JWTConfig jwtConfig;
    private ObjectMapper objectMapper;
    
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTConfig jwtConfig) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;

        this.setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher(jwtConfig.getUri() + "/login", "POST"));
    }

    /**
     * Attempt to Authenticate the request.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            User credentials = new ObjectMapper().
                    readValue(request.getInputStream(), User.class);

            log.debug("Found credentials, authenticating user: {}", credentials);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getEmail(),
                            credentials.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException exception) {
            log.error("Exception occurred while authenticating user", exception);
            throw new RuntimeException(exception);
        }
    }

    /**
     * Create JWT token and write it to response header upon successful authentication.
     * Sends the JWT token in the header, in the response
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException {
        String email = ((UserDetailsImpl) auth.getPrincipal()).getUsername();

        String token = JWT.create()
                .withSubject(email)
                .withClaim("email", email)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .sign(Algorithm.HMAC512(jwtConfig.getSecret()
                                                .getBytes()));

        log.debug("Successfully created token for user (email:{})", email);
        
        response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        
        log.debug("Successfully authenticated user. Wrote token to header. (email:{})", email);
    }
}