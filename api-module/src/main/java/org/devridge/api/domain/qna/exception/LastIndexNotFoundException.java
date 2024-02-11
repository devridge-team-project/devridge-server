package org.devridge.api.domain.qna.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LastIndexNotFoundException extends RuntimeException {

    public LastIndexNotFoundException() {
        super("최신순 정렬 시에는 마지막 게시물의 인덱스 값이 필요합니다.");
    }
}
