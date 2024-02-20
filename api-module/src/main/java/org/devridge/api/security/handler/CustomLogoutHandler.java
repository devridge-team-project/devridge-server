package org.devridge.api.security.handler;

import io.jsonwebtoken.Claims;
import org.devridge.api.domain.auth.repository.RefreshTokenRepository;
import org.devridge.api.util.AccessTokenUtil;
import org.devridge.api.util.JwtUtil;
import org.devridge.api.util.RefreshTokenUtil;
import org.devridge.api.util.ResponseUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomLogoutHandler implements LogoutSuccessHandler {

    private RefreshTokenRepository refreshTokenRepository;

    public CustomLogoutHandler(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String accessToken = AccessTokenUtil.extractAccessTokenFromRequest(request);

        Claims claims = AccessTokenUtil.getClaimsFromAccessToken(accessToken);
        Long refreshTokenId = RefreshTokenUtil.getRefreshTokenIdFromClaims(claims);

        refreshTokenRepository.findById(refreshTokenId).ifPresent(refreshToken -> refreshTokenRepository.delete(refreshToken));
        ResponseCookie responseCookie = JwtUtil.generateRefreshTokenCookie(null, 0);

        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        ResponseUtil.createResponseBody(response, HttpStatus.OK);
    }

}
