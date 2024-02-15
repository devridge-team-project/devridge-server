package org.devridge.api.domain.sociallogin.context;

import org.devridge.api.domain.sociallogin.dto.request.SocialLoginRequest;
import org.devridge.api.domain.sociallogin.dto.response.oauth.OAuth2TokenResponse;
import org.devridge.api.domain.sociallogin.strategy.token.GoogleTokenProvider;
import org.devridge.api.domain.sociallogin.strategy.token.KakaoTokenProvider;
import org.devridge.api.domain.sociallogin.strategy.token.NaverTokenProvider;
import org.devridge.api.domain.sociallogin.strategy.token.OAuth2TokenStrategy;
import org.devridge.api.domain.sociallogin.exception.SocialLoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OAuth2TokenContext {

    private Map<String, OAuth2TokenStrategy> strategies;

    @Autowired
    public OAuth2TokenContext(
            NaverTokenProvider naverTokenProvider,
            GoogleTokenProvider googleTokenProvider,
            KakaoTokenProvider kakaoTokenProvider)
    {
        this.strategies = Map.of(
                "naver", naverTokenProvider,
                "google", googleTokenProvider,
                "kakao", kakaoTokenProvider
        );
    }

    public OAuth2TokenResponse getTokenFromAuthServer(SocialLoginRequest loginRequest) {
        String provider = loginRequest.getProvider();
        OAuth2TokenStrategy strategy = strategies.get(provider);

        if (strategy == null) {
            throw new SocialLoginException(400, "Unknown Provider: " + loginRequest.getProvider());
        }

        return strategy.getToken(loginRequest);
    }
}
