package org.devridge.api.domain.sociallogin.exception;

import org.devridge.api.exception.common.BaseException;

public class SocialLoginException extends BaseException {

    public SocialLoginException(int code, String message) {
        super(code, message);
    }
}
