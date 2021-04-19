package com.ntnu.gidd.service.token;

import com.ntnu.gidd.model.RefreshToken;
import com.ntnu.gidd.security.token.JwtRefreshToken;
import com.ntnu.gidd.security.token.JwtToken;

public interface RefreshTokenService {

    RefreshToken saveRefreshToken(JwtToken token);

}
