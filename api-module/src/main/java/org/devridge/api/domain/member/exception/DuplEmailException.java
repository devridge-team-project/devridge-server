package org.devridge.api.domain.member.exception;

import org.devridge.api.common.exception.common.BaseException;

public class DuplEmailException extends BaseException {

    public DuplEmailException(int code, String message) {
        super(code, message);
    }
}