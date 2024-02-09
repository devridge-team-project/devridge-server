package org.devridge.api.exception.common;

public class ConflictException extends BaseException {

    public ConflictException() {
        super(409, "이미 존재하는 데이터입니다. 다른 데이터를 사용해주세요.");
    }
}
