package org.devridge.api.common.util;

import io.jsonwebtoken.Claims;

public class RefreshTokenUtil {
    public static Long getRefreshTokenIdFromClaims(Claims claims) {
        return ((Integer)claims.get("refreshTokenId")).longValue();
    }
}
