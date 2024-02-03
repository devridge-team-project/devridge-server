package org.devridge.api.domain.sociallogin.strategy.token;

import org.devridge.api.domain.sociallogin.dto.request.SocialLoginRequest;
import org.devridge.api.domain.sociallogin.dto.response.OAuth2TokenResponse;

public interface OAuth2TokenStrategy {

    OAuth2TokenResponse getToken(SocialLoginRequest request);
}