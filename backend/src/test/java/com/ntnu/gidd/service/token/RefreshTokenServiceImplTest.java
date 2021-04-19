package com.ntnu.gidd.service.token;

import com.ntnu.gidd.config.JwtConfiguration;
import com.ntnu.gidd.model.RefreshToken;
import com.ntnu.gidd.repository.RefreshTokenRepository;
import com.ntnu.gidd.security.UserDetailsImpl;
import com.ntnu.gidd.security.config.JWTConfig;
import com.ntnu.gidd.security.token.JwtRefreshToken;
import com.ntnu.gidd.security.token.JwtTokenFactory;
import com.ntnu.gidd.security.token.RawJwtAccessToken;
import com.ntnu.gidd.security.token.TokenFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@Import({JwtConfiguration.class})
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class RefreshTokenServiceImplTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private RefreshTokenService refreshTokenService;

    private TokenFactory tokenFactory;

    private JWTConfig jwtConfig;

    private RefreshToken refreshToken;

    private JwtRefreshToken jwtRefreshToken;

    @BeforeEach
    void setUp() {
        jwtConfig = new JWTConfig();
        refreshTokenService = new RefreshTokenServiceImpl(refreshTokenRepository, jwtConfig);
        ReflectionTestUtils.setField(jwtConfig, "secret", "notasecret");
        ReflectionTestUtils.setField(jwtConfig, "expiration", 24*60*60);
        ReflectionTestUtils.setField(jwtConfig, "refreshExpiration", 24*60*60);
        tokenFactory = new JwtTokenFactory(jwtConfig);

        UserDetails userDetails = UserDetailsImpl.builder()
                .email("test@mail.com")
                .build();

        RawJwtAccessToken rawJwtAccessToken = new RawJwtAccessToken(tokenFactory.createRefreshToken(userDetails).getToken());
        jwtRefreshToken = JwtRefreshToken.of(rawJwtAccessToken, jwtConfig.getSecret()).get();

        refreshToken = RefreshToken.builder()
                .jti(UUID.fromString(jwtRefreshToken.getJti()))
                .isValid(true)
                .build();

        when(refreshTokenRepository.save(refreshToken)).thenReturn(refreshToken);
    }

    @Test
    void testSaveRefreshTokenSavesAndReturnsRefreshToken() {
        RefreshToken actualRefreshToken = refreshTokenService.saveRefreshToken(jwtRefreshToken);

        assertThat(actualRefreshToken).isEqualTo(refreshToken);
    }
}