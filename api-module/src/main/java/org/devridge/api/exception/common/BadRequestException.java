package org.devridge.api.exception.common;

public class BadRequestException extends BaseException {

    public BadRequestException() {
        super(400, "요청 값이 잘못되었습니다. 다시 입력해주세요.");
    }
}
