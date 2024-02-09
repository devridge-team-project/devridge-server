package org.devridge.api.exception.member;

import org.devridge.api.exception.common.BaseException;


public class AccessTokenInvalidException extends BaseException {

    public AccessTokenInvalidException(int code, String message) {
        super(code, message);
    }
}
