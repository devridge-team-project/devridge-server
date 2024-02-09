package org.devridge.api.exception.member;

import org.devridge.api.exception.common.BaseException;

public class WrongLoginException extends BaseException {

    public WrongLoginException(int code, String message) {
        super(code, message);
    }
}