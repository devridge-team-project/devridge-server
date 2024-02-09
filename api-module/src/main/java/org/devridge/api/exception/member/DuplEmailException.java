package org.devridge.api.exception.member;

import org.devridge.api.exception.common.BaseException;

public class DuplEmailException extends BaseException {

    public DuplEmailException(int code, String message) {
        super(code, message);
    }
}