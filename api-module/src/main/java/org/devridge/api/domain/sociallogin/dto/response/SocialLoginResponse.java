package org.devridge.api.domain.sociallogin.dto.response;

import lombok.Getter;

@Getter
public class SocialLoginResponse {

    private String token;
    private boolean redirect;

    public SocialLoginResponse(String token, boolean redirect) {
        this.token = token;
        this.redirect = redirect;
    }
}
