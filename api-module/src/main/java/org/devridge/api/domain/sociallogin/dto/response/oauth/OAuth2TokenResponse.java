package org.devridge.api.domain.sociallogin.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class OAuth2TokenResponse {

    @JsonProperty("access_token")
    String accessToken;

    @JsonProperty("token_type")
    String tokenType;

    @JsonProperty("refresh_token")
    String refreshToken;

    @JsonProperty("expires_in")
    String expiresIn;

    @JsonProperty("scope")
    String scope;

    @JsonProperty("refresh_token_expires_in")
    String refreshTokenExpiresIn;
}