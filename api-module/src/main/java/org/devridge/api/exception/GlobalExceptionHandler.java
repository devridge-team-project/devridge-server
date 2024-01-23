package org.devridge.api.exception;

import org.devridge.api.exception.member.*;
import org.devridge.common.dto.BaseErrorResponse;
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
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    public ResponseEntity<BaseErrorResponse> handleException(PasswordNotMatchException e) {
        BaseErrorResponse response = new BaseErrorResponse("password does not match.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SkillsNotValidException.class)
    public ResponseEntity<BaseErrorResponse> handleException(SkillsNotValidException e) {
        BaseErrorResponse response = new BaseErrorResponse("skills not valid.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WeakPasswordException.class)
    public ResponseEntity<BaseErrorResponse> handleException(WeakPasswordException e) {
        BaseErrorResponse response = new BaseErrorResponse("weak password.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplEmailException.class)
    public ResponseEntity<BaseErrorResponse> handleException(DuplEmailException e) {
        BaseErrorResponse response = new BaseErrorResponse("email already exists.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplNicknameException.class)
    public ResponseEntity<BaseErrorResponse> handleException(DuplNicknameException e) {
        BaseErrorResponse response = new BaseErrorResponse("nickname already exists.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<BaseErrorResponse> handleException(MemberNotFoundException e) {
        BaseErrorResponse response = new BaseErrorResponse("member not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
