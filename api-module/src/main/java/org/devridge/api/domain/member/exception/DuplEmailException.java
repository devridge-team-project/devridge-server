package org.devridge.api.domain.member.exception;

import org.devridge.api.exception.common.BaseException;

public class DuplEmailException extends BaseException {

    public DuplEmailException(int code, String message) {
        super(code, message);
    }
}