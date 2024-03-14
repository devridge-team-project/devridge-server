package org.devridge.api.domain.note.exception;


import org.devridge.api.common.exception.common.BaseException;

public class NoteForbiddenException extends BaseException {

    public NoteForbiddenException(int code, String message) {
        super(code, message);
    }
}
