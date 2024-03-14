package org.devridge.api.common.exception.common;

public class InternalServerErrorException extends BaseException {

    public InternalServerErrorException() {
        super(500, "서버에 이상이 발생하여 해당 요청에 실패했습니다.");
    }
}
