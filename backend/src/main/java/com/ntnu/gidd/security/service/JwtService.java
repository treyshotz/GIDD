package com.ntnu.gidd.security.service;

import com.ntnu.gidd.security.token.JwtToken;

public interface JwtService {

    JwtToken refreshToken(String header);

}
