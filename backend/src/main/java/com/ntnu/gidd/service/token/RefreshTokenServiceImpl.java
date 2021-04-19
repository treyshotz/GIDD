package com.ntnu.gidd.service.token;

import com.ntnu.gidd.model.RefreshToken;
import com.ntnu.gidd.repository.RefreshTokenRepository;
import com.ntnu.gidd.security.config.JWTConfig;
import com.ntnu.gidd.security.token.JwtRefreshToken;
import com.ntnu.gidd.security.token.JwtToken;
import com.ntnu.gidd.security.token.RawJwtAccessToken;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private RefreshTokenRepository refreshTokenRepository;

    private JWTConfig jwtConfig;

    @Override
    public RefreshToken saveRefreshToken(JwtToken refreshToken) {
        JwtRefreshToken jwtRefreshToken = parseToken(refreshToken);
        RefreshToken refreshTokenToSave = buildRefreshToken(jwtRefreshToken);
        RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshTokenToSave);

        log.debug("[X] Successfully saved refresh token (jti: {})", savedRefreshToken.getJti());
        return savedRefreshToken;
    }

    private JwtRefreshToken parseToken(JwtToken refreshToken) {
        RawJwtAccessToken rawJwtAccessToken = new RawJwtAccessToken(refreshToken.getToken());
        return JwtRefreshToken.of(rawJwtAccessToken, jwtConfig.getSecret())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token claims."));
    }

    private RefreshToken buildRefreshToken(JwtRefreshToken jwtRefreshToken) {
        return RefreshToken.builder()
                .jti(UUID.fromString(jwtRefreshToken.getJti()))
                .isValid(true)
                .build();
    }

}
