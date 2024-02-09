package org.devridge.api.exception.member;

import org.devridge.api.exception.common.BaseException;

public class PasswordNotMatchException extends BaseException {

    public PasswordNotMatchException(int code, String message) {
        super(code, message);
    }
}
