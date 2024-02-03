package org.devridge.api.domain.sociallogin.dto.response;

import lombok.Getter;

@Getter
public class OAuth2TokenResponse {

    String accessToken;
    String tokenType;
    String refreshToken;
    String expiresIn;
    String scope;
    String refreshTokenExpiresIn;
}
