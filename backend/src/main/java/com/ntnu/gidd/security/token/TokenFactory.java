package com.ntnu.gidd.security.token;

import org.springframework.security.core.userdetails.UserDetails;

public interface TokenFactory {
    JwtAccessToken createAccessToken(UserDetails userDetails);

    JwtToken createRefreshToken(UserDetails userDetails);
}
