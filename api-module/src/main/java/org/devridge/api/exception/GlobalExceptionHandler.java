package org.devridge.api.exception;

import org.devridge.api.exception.member.PasswordNotMatchException;
import org.devridge.api.exception.member.SkillsNotValidException;
import org.devridge.api.exception.member.WeakPasswordException;
import org.devridge.common.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<BaseResponse> handleException(NoSuchElementException e) {
        BaseResponse response = new BaseResponse(HttpStatus.NOT_FOUND.value(), "해당 요소를 찾을 수 없습니다.");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    public ResponseEntity<BaseResponse> handleException(PasswordNotMatchException e) {
        BaseResponse response = new BaseResponse(HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SkillsNotValidException.class)
    public ResponseEntity<BaseResponse> handleException(SkillsNotValidException e) {
        BaseResponse response = new BaseResponse(HttpStatus.BAD_REQUEST.value(), "사버에 존재하지 않는 직군이 포함되어 있습니다. 다시 확인 바랍니다.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WeakPasswordException.class)
    public ResponseEntity<BaseResponse> handleException(WeakPasswordException e) {
        BaseResponse response = new BaseResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
