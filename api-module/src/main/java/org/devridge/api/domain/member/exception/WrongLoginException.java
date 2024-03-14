package org.devridge.api.domain.member.exception;

import org.devridge.api.common.exception.common.BaseException;

public class WrongLoginException extends BaseException {

    public WrongLoginException(int code, String message) {
        super(code, message);
    }
}