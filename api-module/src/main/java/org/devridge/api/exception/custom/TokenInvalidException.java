package org.devridge.api.exception.custom;

import org.devridge.api.exception.common.BaseException;

public class TokenInvalidException extends BaseException {

    public TokenInvalidException() {
        super(401, "Invalid Token!");
    }

    public TokenInvalidException(String message) {
        super(401, message);
    }
}