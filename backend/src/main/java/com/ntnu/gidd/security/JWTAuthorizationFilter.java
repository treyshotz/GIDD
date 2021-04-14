package com.ntnu.gidd.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {


    private JWTConfig jwtConfig;


    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTConfig jwtConfig) {
        super(authenticationManager);
        this.jwtConfig = jwtConfig;
    }

    /**
     * Read authentication from request header and add to security context.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(jwtConfig.getHeader());

        if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    /**
     * Read the JWT from Authorization header and validate the token.
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(jwtConfig.getHeader());

        if (token != null) {
            String user = JWT.require(Algorithm.HMAC512(jwtConfig.getSecret().getBytes()))
                    .build()
                    .verify(token.replace(jwtConfig.getPrefix(), ""))
                    .getSubject();

            if (user != null)
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());

        }

        return null;
    }
}
