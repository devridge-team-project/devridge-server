package org.devridge.api.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.devridge.api.common.security.auth.AuthProperties;

import javax.servlet.http.Cookie;
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

    public static String extractAccessTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("dev-access".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null; // 해당 이름의 쿠키가 없을 경우 null을 반환
    }
}
