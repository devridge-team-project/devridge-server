package org.devridge.api.domain.sociallogin.dto.response;

public class SocialLoginResponse {

    private String accessToken;

    public SocialLoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
