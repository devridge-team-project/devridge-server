package org.devridge.api.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.devridge.api.common.security.auth.AuthProperties;

import javax.servlet.http.HttpServletRequest;

public class AccessTokenUtil {
    public static Claims getClaimsFromAccessToken(String accessToken) {
        return Jwts.parserBuilder().setSigningKey(AuthProperties.getAccessSecret()).build().parseClaimsJws(accessToken).getBody();
    }

    public static String extractAccessTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7).trim();
        }
        return null;
    }
}
