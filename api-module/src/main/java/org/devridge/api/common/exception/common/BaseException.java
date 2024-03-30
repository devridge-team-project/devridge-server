package org.devridge.api.common.exception.common;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    int code;

    public BaseException(int code, String message) {
        super(message);
        this.code = code;
    }
}
