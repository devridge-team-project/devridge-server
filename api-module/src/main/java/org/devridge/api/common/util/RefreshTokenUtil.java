package org.devridge.api.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import org.devridge.api.common.security.auth.AuthProperties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class RefreshTokenUtil {
    public static Long getRefreshTokenIdFromClaims(Claims claims) {
        return ((Integer)claims.get("refreshTokenId")).longValue();
    }

    public static Claims getClaimsFromRefreshToken(String refreshToken) {
        return Jwts.parserBuilder().setSigningKey(AuthProperties.getRefreshSecret()).build().parseClaimsJws(refreshToken).getBody();
    }

    public static String extractRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("dev-refresh".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static boolean checkIfRefreshTokenValid(String refreshToken) {
        Claims claims = null;

        try {
            claims = getClaimsFromRefreshToken(refreshToken);
        } catch (ExpiredJwtException e) { // 리프레시 토큰 만료
            return false;
        } catch (MalformedJwtException e) { // 위변조 검사
            return false;
        }
        return true;
    }
}
