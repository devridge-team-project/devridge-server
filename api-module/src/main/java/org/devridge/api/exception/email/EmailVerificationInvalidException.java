package org.devridge.api.exception.email;

import org.devridge.api.exception.common.BaseException;

public class EmailVerificationInvalidException extends BaseException {

    public EmailVerificationInvalidException(int code, String message) {
        super(code, message);
    }
}
