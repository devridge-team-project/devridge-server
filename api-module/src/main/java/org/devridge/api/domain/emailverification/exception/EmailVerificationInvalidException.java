package org.devridge.api.domain.emailverification.exception;

import org.devridge.api.common.exception.common.BaseException;

public class EmailVerificationInvalidException extends BaseException {

    public EmailVerificationInvalidException(int code, String message) {
        super(code, message);
    }
}
