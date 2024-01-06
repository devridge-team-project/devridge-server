package org.devridge.api.util;

import lombok.NoArgsConstructor;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.security.auth.AuthProperties;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@NoArgsConstructor
public class JwtUtil {
    private static long TOKEN_VALIDITY_TIME_IN_SECONDS = 1000;
    private static long TOKEN_VALIDITY_TIME_IN_MINUTES = TOKEN_VALIDITY_TIME_IN_SECONDS * 60;
    private static long TOKEN_VALIDITY_TIME_IN_HOURS = TOKEN_VALIDITY_TIME_IN_MINUTES * 60;

    private static long ACCESS_TOKEN_VALIDITY_TIME = TOKEN_VALIDITY_TIME_IN_SECONDS * 5; // 5초(임시)
    private static long REFRESH_TOKEN_VALIDITY_TIME = TOKEN_VALIDITY_TIME_IN_SECONDS * 9;  // 9초 (임시)

    public static String createAccessToken(Member member, Long refreshTokenId) {
        return Jwts.builder()
                .setSubject("accessToken")
                .setClaims(createAccessTokenClaims(member, refreshTokenId))
                .setExpiration(createTokenExpiration(ACCESS_TOKEN_VALIDITY_TIME))
                .signWith(createSigningKey(AuthProperties.getAccessSecret()), SignatureAlgorithm.HS256)
                .compact();
    }

    public static String createRefreshToken(Member member) {
        return Jwts.builder()
                .setSubject("refreshToken")
                .setClaims(createRefreshTokenClaims(member))
                .setExpiration(createTokenExpiration(REFRESH_TOKEN_VALIDITY_TIME))
                .signWith(createSigningKey(AuthProperties.getRefreshSecret()), SignatureAlgorithm.HS256)
                .compact();
    }

    public static ResponseCookie makeResponseCookie(String accessToken) {
        ResponseCookie cookies = ResponseCookie.from("plac_token", accessToken)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path("/")
                .maxAge(ACCESS_TOKEN_VALIDITY_TIME)     // 15분
                .build();
        return cookies;
    }

    private static Date createTokenExpiration(long expirationTime) {
        Date expiration = new Date(System.currentTimeMillis() + expirationTime);
        return expiration;
    }

    // BASE64로 인코딩된 문자열을 디코딩하여 대칭키를 생성한다. 이 키는 JWT 서명 과정에서 사용된다.
    private static Key createSigningKey(String tokenSecret) {
        // 입력된 tokenSecret은 BASE64로 인코딩되어 있으므로, 먼저 디코딩하여 원래의 바이트 배열 형태로 복원한다.
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 인가 필터 - access token 만료 시 refreshToken의 유효성을 쉽게 조회하기 위해 refresh token id도 함께 넣어준다
    private static Map<String, Object> createAccessTokenClaims(Member member, Long refreshTokenId) {
        Map<String, Object> map = new HashMap<>();
        map.put("memberEmail", member.getEmail());
        map.put("provider", member.getProvider());
        map.put("refreshTokenId", refreshTokenId);
        return map;
    }

    private static Map<String, Object> createRefreshTokenClaims(Member member) {
        Map<String, Object> map = new HashMap<>();
        map.put("memberEmail", member.getEmail());
        return map;
    }
}
