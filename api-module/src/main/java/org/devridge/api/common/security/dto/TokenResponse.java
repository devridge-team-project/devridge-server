package org.devridge.api.common.security.dto;

import lombok.Getter;

@Getter
public class TokenResponse {

    private String accessToken;

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
