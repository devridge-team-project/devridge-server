package org.devridge.api.exception.member;

import org.devridge.api.exception.common.BaseException;

public class SkillsNotValidException extends BaseException {

    public SkillsNotValidException(int code, String message) {
        super(code, message);
    }
}