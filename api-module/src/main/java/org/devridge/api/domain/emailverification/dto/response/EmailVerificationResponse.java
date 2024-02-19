package org.devridge.api.domain.emailverification.dto.response;

import lombok.Getter;

@Getter
public class EmailVerificationResponse {
    String token;

    public EmailVerificationResponse(String token) {
        this.token = token;
    }
}
