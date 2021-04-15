package com.ntnu.gidd.util;

import lombok.AllArgsConstructor;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ntnu.gidd.security.JWTConfig;

@AllArgsConstructor
public class JwtUtil {
	
	private JWTConfig jwtConfig;
	
	public String decodeToken(String encodedToken) {
		return JWT.require(Algorithm.HMAC512(jwtConfig.getSecret().getBytes()))
				.build()
				.verify(encodedToken.replace(jwtConfig.getPrefix(), ""))
				.getSubject();
	}
}
