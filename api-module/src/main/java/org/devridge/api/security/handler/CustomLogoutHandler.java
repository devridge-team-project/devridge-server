package org.devridge.api.security.handler;

import io.jsonwebtoken.Claims;
import org.devridge.api.domain.member.entity.RefreshToken;
import org.devridge.api.domain.member.repository.RefreshTokenRepository;
import org.devridge.api.util.AccessTokenUtil;
import org.devridge.api.util.RefreshTokenUtil;
import org.devridge.api.util.ResponseUtil;
import org.devridge.common.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class CustomLogoutHandler implements LogoutSuccessHandler {

    private RefreshTokenRepository refreshTokenRepository;

    public CustomLogoutHandler(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("=== CustomLogOutHandler - onLogoutSuccess() ===");
        String accessToken = AccessTokenUtil.extractAccessTokenFromRequest(request);

        Claims claims = AccessTokenUtil.getClaimsFromAccessToken(accessToken);
        Long refreshTokenId = RefreshTokenUtil.getRefreshTokenIdFromClaims(claims);

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findById(refreshTokenId);
        if (refreshToken.isPresent()) {
            refreshTokenRepository.delete(refreshToken.get());
        }

        BaseResponse baseResponse = new BaseResponse(
                HttpStatus.OK.value(),
                "logout success"
        );

        ResponseUtil.createResponseMessage(response, baseResponse);
    }

}
