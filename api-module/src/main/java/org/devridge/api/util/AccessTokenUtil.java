package org.devridge.api.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.devridge.api.security.auth.AuthProperties;

import javax.servlet.http.HttpServletRequest;

public class AccessTokenUtil {
    public static Claims getClaimsFromAccessToken(String accessToken) {
        return Jwts.parserBuilder().setSigningKey(AuthProperties.getAccessSecret()).build().parseClaimsJws(accessToken).getBody();
    }

    public static String extractAccessTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization").trim();
        if(bearerToken != null && !bearerToken.trim().isEmpty() && bearerToken.startsWith("Bearer ")){
            String accessToken = bearerToken.substring(7);
            return accessToken;
        }else return null;
    }
}
