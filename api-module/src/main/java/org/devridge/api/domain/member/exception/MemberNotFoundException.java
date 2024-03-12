package org.devridge.api.domain.member.exception;

import org.devridge.api.common.exception.common.BaseException;

public class MemberNotFoundException extends BaseException {

    public MemberNotFoundException(int code, String message) {
        super(code, message);
    }
}
