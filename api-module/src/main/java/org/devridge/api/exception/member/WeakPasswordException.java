package org.devridge.api.exception.member;

import org.devridge.api.exception.common.BaseException;

public class WeakPasswordException extends BaseException {
    public WeakPasswordException(int code, String message) {
        super(code, message);
    }
}
