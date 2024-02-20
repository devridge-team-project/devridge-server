package org.devridge.api.exception.common;

public class TokenInvalidException extends BaseException {

    public TokenInvalidException() {
        super(403, "Invalid Token!");
    }
}