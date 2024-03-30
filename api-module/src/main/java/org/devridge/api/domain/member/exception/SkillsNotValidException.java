package org.devridge.api.domain.member.exception;

import org.devridge.api.common.exception.common.BaseException;

public class SkillsNotValidException extends BaseException {

    public SkillsNotValidException(int code, String message) {
        super(code, message);
    }
}