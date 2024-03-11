package org.devridge.api.domain.sociallogin.dto.response;

import lombok.Getter;

@Getter
public class SocialLoginRedirect {

    private String tempJwt;

    public SocialLoginRedirect(String tempJwt) {
        this.tempJwt = tempJwt;
    }
}
