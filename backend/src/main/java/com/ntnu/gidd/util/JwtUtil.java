package com.ntnu.gidd.util;

import com.ntnu.gidd.security.config.JWTConfig;
import com.ntnu.gidd.security.token.JwtRefreshToken;
import com.ntnu.gidd.security.token.RawJwtAccessToken;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Optional;


@AllArgsConstructor
public class JwtUtil {
	
	private JWTConfig jwtConfig;
	
	public String getEmailFromToken(String token) {
		return Jwts.parser()
				.setSigningKey(jwtConfig.getSecret())
				.parseClaimsJws(token.replace(jwtConfig.getPrefix(), ""))
				.getBody().getSubject();
	}

	public boolean isValidToken(String token) {
		try {
			Jwts.parser()
					.setSigningKey(jwtConfig.getSecret())
					.parseClaimsJws(token.replace(jwtConfig.getPrefix(), ""));
			return true;
		} catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
			throw new BadCredentialsException("Invalid credentials", ex);
		}
	}

	public Optional<JwtRefreshToken> parseToken(String token) {
		RawJwtAccessToken rawJwtAccessToken = new RawJwtAccessToken(token);
		return JwtRefreshToken.of(rawJwtAccessToken, jwtConfig.getSecret());
	}
}
