package org.devridge.api.exception.sociallogin;

import org.devridge.api.exception.common.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SocialLoginException extends BaseException {

    public SocialLoginException(int code, String message) {
        super(code, message);
    }
}
