package org.devridge.api.exception.member;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SkillsNotValidException extends RuntimeException {

    public SkillsNotValidException() {
        super();
    }
}
