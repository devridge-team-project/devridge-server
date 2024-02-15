package org.devridge.api.exception.common;

public class DataNotFoundException extends BaseException {

    public DataNotFoundException() {
        super(404, "해당 데이터가 삭제되었거나 존재하지 않습니다.");
    }
}
