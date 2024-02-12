package org.devridge.api.exception.common;

public class PutOrDeleteForbiddenException extends BaseException {

    public PutOrDeleteForbiddenException() {
        super(403, "해당 데이터에 대한 삭제 및 수정 권한이 존재하지 않습니다.");
    }
}
