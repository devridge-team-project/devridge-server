package org.devridge.api.domain.sociallogin.dto.response;

import lombok.Getter;

@Getter
public class SocialLoginRedirect {

    private String redirectUri;

    public SocialLoginRedirect(String redirectUri) {
        this.redirectUri = redirectUri;
    }
}
