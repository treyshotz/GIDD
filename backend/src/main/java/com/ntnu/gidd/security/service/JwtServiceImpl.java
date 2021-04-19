package com.ntnu.gidd.security.service;

import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.UserRepository;
import com.ntnu.gidd.security.UserDetailsImpl;
import com.ntnu.gidd.security.config.JWTConfig;
import com.ntnu.gidd.security.extractor.TokenExtractor;
import com.ntnu.gidd.security.token.JwtToken;
import com.ntnu.gidd.security.token.RawJwtAccessToken;
import com.ntnu.gidd.security.token.JwtRefreshToken;
import com.ntnu.gidd.security.token.TokenFactory;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class JwtServiceImpl implements JwtService {

    private JWTConfig jwtConfig;
    private TokenFactory tokenFactory;
    private TokenExtractor tokenExtractor;
    private UserRepository userRepository;

    /**
     * Create a new jwt access token from the refresh token in the request header.
     *
     * @return the new jwt access token
     */
    @Override
    public JwtToken refreshToken(String header) {
        String token = tokenExtractor.extract(header);
        RawJwtAccessToken rawJwtAccessToken = new RawJwtAccessToken(token);
        JwtRefreshToken jwtRefreshToken = JwtRefreshToken.of(rawJwtAccessToken, jwtConfig.getSecret())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token claims."));

        String subject = jwtRefreshToken.getSubject();
        User user = userRepository.findByEmail(subject)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + subject));

        UserDetails userDetails = UserDetailsImpl.builder()
                .email(user.getEmail())
                .build();

        return tokenFactory.createAccessToken(userDetails);
    }
}
