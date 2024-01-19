package org.devridge.api.exception;

import java.util.NoSuchElementException;
import javax.persistence.EntityNotFoundException;
import org.devridge.api.exception.member.DuplEmailException;
import org.devridge.api.exception.member.MemberNotFoundException;
import org.devridge.api.exception.member.PasswordNotMatchException;
import org.devridge.api.exception.member.SkillsNotValidException;
import org.devridge.api.exception.member.WeakPasswordException;
import org.devridge.common.dto.BaseResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
        BaseResponse response = new BaseResponse(HttpStatus.BAD_REQUEST.value(),
            "서버에 존재하지 않는 직군이 포함되어 있습니다. 다시 확인 바랍니다.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WeakPasswordException.class)
    public ResponseEntity<BaseResponse> handleException(WeakPasswordException e) {
        BaseResponse response = new BaseResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplEmailException.class)
    public ResponseEntity<BaseResponse> handleException(DuplEmailException e) {
        BaseResponse response = new BaseResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<BaseResponse> handleException(MemberNotFoundException e) {
        BaseResponse response = new BaseResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<BaseResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        BaseResponse response = new BaseResponse(HttpStatus.NOT_FOUND.value(),
            "해당 엔티티를 찾을 수 없습니다.");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<BaseResponse> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        BaseResponse response = new BaseResponse(HttpStatus.CONFLICT.value(), "이미 존재하는 데이터입니다.");
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse> handleAccessDeniedException(AccessDeniedException e) {
        BaseResponse response = new BaseResponse(HttpStatus.FORBIDDEN.value(), "거부된 접근입니다.");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleOtherExceptions(Exception e) {
        BaseResponse response = new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "오류가 발생했습니다.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
