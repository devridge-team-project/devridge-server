package org.devridge.api.exception.member;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AccessTokenInvalidException extends RuntimeException {

    public AccessTokenInvalidException() {
        super();
    }
}
