package org.devridge.api.domain.note.exception;

import org.devridge.api.common.exception.common.BaseException;

public class ParticipationNoteForbiddenException extends BaseException {

    public ParticipationNoteForbiddenException(int code, String message) {
        super(code, message);
    }
}
