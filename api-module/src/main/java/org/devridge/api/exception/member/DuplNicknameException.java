package org.devridge.api.exception.member;

import org.devridge.api.exception.common.BaseException;

public class DuplNicknameException extends BaseException {

    public DuplNicknameException(int code, String message) {
        super(code, message);
    }
}
