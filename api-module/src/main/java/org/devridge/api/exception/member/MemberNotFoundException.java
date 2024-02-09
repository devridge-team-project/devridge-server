package org.devridge.api.exception.member;

import org.devridge.api.exception.common.BaseException;

public class MemberNotFoundException extends BaseException {

    public MemberNotFoundException(int code, String message) {
        super(code, message);
    }
}
