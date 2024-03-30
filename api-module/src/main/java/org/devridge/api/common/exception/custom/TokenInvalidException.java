package org.devridge.api.common.exception.custom;

import org.devridge.api.common.exception.common.BaseException;

public class TokenInvalidException extends BaseException {

    public TokenInvalidException() {
        super(401, "Invalid Token!");
    }

    public TokenInvalidException(String message) {
        super(401, message);
    }
}