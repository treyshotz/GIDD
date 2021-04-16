package com.ntnu.gidd.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Optional;


/**
 * Implementation of {@link JwtToken} for refresh tokens.
 */
@Getter
@AllArgsConstructor
public class RefreshToken implements JwtToken {

    private Jws<Claims> claims;

    public static Optional<RefreshToken> of(RawJwtAccessToken token, String signingKey) {
        Jws<Claims> claims = token.parseClaims(signingKey);

        List<String> scopes = claims.getBody()
                .get("scopes", List.class);
        boolean hasRefreshTokenScope = scopes != null && scopes.stream()
                .anyMatch(Scopes.REFRESH_TOKEN.scope()::equals);

        if (hasRefreshTokenScope)
            return Optional.of(new RefreshToken(claims));

        return Optional.empty();
    }

    @Override
    public String getToken() {
        return null;
    }

    public String getSubject() {
        return claims.getBody().getSubject();
    }
}
