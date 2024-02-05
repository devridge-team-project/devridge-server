package org.devridge.api.domain.sociallogin.dto.response;

import lombok.Getter;

@Getter
public class SocialLoginResponse {

    private String accessToken;
    private String redirectUri;
    private boolean redirect;

    public SocialLoginResponse(String accessToken, String redirectUri, boolean redirect) {
        this.accessToken = accessToken;
        this.redirectUri = redirectUri;
        this.redirect = redirect;
    }
}
