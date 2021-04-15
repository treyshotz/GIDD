package com.ntnu.gidd.security;

import com.ntnu.gidd.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private JwtUtil jwtUtil;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTConfig jwtConfig) {
        super(authenticationManager);
        this.jwtConfig = jwtConfig;
        this.jwtUtil = new JwtUtil(jwtConfig);
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
            String user = jwtUtil.decodeToken(token);

            if (user != null)
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());

        }

        return null;
    }
}
