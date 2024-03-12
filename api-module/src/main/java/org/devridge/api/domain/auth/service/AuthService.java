package org.devridge.api.domain.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.auth.entity.RefreshToken;
import org.devridge.api.domain.auth.repository.RefreshTokenRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.common.exception.common.DataNotFoundException;
import org.devridge.api.common.exception.common.UnAuthorizedException;
import org.devridge.api.common.exception.custom.TokenInvalidException;
import org.devridge.api.common.security.auth.AuthProperties;
import org.devridge.api.common.security.dto.TokenResponse;
import org.devridge.api.common.util.AccessTokenUtil;
import org.devridge.api.common.util.JwtUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    public TokenResponse reIssueAccessToken(HttpServletRequest request) {
        String accessToken = AccessTokenUtil.extractAccessTokenFromRequest(request);

        if (accessToken == null) {
            throw new UnAuthorizedException();
        }

        Claims claims = validateAccessToken(accessToken);
        Long refreshTokenId = checkRefreshToken(request, claims);

        Member member = getMemberFromAccessToken(claims);
        String newAccessToken = JwtUtil.createAccessToken(member, refreshTokenId);

        return new TokenResponse(newAccessToken);
    }

    private Long checkRefreshToken(HttpServletRequest request, Claims claims) {
        Long refreshTokenId = ((Integer) claims.get("refreshTokenId")).longValue();

        RefreshToken refreshToken = refreshTokenRepository.findById(refreshTokenId)
                .orElseThrow(() -> new UnAuthorizedException());

        String refreshTokenValue = refreshToken.getRefreshToken();

        if (!refreshTokenValue.equals(getRefreshTokenFromCookies(request))) {
            throw new TokenInvalidException();
        }
        validateRefreshToken(refreshToken);

        return refreshTokenId;
    }

    private Member getMemberFromAccessToken(Claims claims) {
        String memberEmail = (String) claims.get("memberEmail");
        String provider = (String) claims.get("provider");

        Member member = memberRepository.findByEmailAndProvider(memberEmail, provider)
                .orElseThrow(() -> new DataNotFoundException());
        return member;
    }

    private String checkIfRefreshTokenMatches(HttpServletRequest request, Long refreshTokenId) {
        RefreshToken refreshToken = refreshTokenRepository.findById(refreshTokenId)
                        .orElseThrow(() -> new DataNotFoundException());
        String refreshTokenValue = refreshToken.getRefreshToken();

        if (!refreshTokenValue.equals(getRefreshTokenFromCookies(request))) {
            throw new TokenInvalidException();
        }

        return refreshToken.getRefreshToken();
    }

    private void validateRefreshToken(RefreshToken refreshToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(AuthProperties.getRefreshSecret()).build()
                    .parseClaimsJws(refreshToken.getRefreshToken())
                    .getBody();
        } catch (ExpiredJwtException e) {
            refreshTokenRepository.delete(refreshToken);
            throw new TokenInvalidException("refresh-token expired");
        } catch (MalformedJwtException e) {
            throw new TokenInvalidException();
        } catch (Exception e) {
            throw new TokenInvalidException();
        }
    }

    private static Claims validateAccessToken(String accessToken) {
        Claims accessTokenClaims = null;
        try {
            accessTokenClaims = AccessTokenUtil.getClaimsFromAccessToken(accessToken);
        } catch (ExpiredJwtException e) {
            accessTokenClaims = e.getClaims();
        } catch (MalformedJwtException e) {
            throw new TokenInvalidException();
        } catch (Exception e) {
            throw new TokenInvalidException();
        }
        return accessTokenClaims;
    }

    private String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie cookie;
        try {
            cookie = Arrays.stream(request.getCookies())
                    .filter(r -> r.getName().equals("devridge"))
                    .findAny()
                    .orElse(null);
        } catch (NullPointerException e) {
            throw new TokenInvalidException();
        }
        return cookie.getValue();
    }
}
