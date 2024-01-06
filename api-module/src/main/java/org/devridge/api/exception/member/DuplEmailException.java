package org.devridge.api.exception.member;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplEmailException extends RuntimeException {
    private String message;
    public DuplEmailException(String message) {
        super(message);
        this.message = message;
    }
}