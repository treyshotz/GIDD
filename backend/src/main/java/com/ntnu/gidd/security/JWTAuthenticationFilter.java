package com.ntnu.gidd.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.ntnu.gidd.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private JWTConfig jwtConfig;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTConfig jwtConfig) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;

        setFilterProcessesUrl(jwtConfig.getUri() + "/login");
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

        response.getWriter()
                .write(token);
        response.getWriter()
                .flush();

        log.debug("Successfully authenticated user. Wrote token to header. (email:{})", email);
    }
}
