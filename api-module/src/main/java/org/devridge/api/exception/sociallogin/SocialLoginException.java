package org.devridge.api.exception.sociallogin;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SocialLoginException extends RuntimeException {

    private String message;

    public SocialLoginException(String message) {
        super(message);
        this.message = message;
    }
}
