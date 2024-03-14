package org.devridge.api.domain.sociallogin.exception;

import org.devridge.api.common.exception.common.BaseException;

public class SocialLoginException extends BaseException {

    public SocialLoginException(int code, String message) {
        super(code, message);
    }
}
