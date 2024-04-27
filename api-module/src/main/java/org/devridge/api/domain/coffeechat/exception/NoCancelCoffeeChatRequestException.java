package org.devridge.api.domain.coffeechat.exception;

import org.devridge.api.common.exception.common.BaseException;

public class NoCancelCoffeeChatRequestException extends BaseException {

    public NoCancelCoffeeChatRequestException(int code, String message) {
        super(code, message);
    }
}
