package org.devridge.api.exception;

import org.devridge.api.exception.member.*;
import org.devridge.common.dto.BaseErrorResponse;
import org.devridge.common.dto.BaseResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<BaseResponse> handleException(NoSuchElementException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    public ResponseEntity<BaseErrorResponse> handleException(PasswordNotMatchException e) {
        BaseErrorResponse response = new BaseErrorResponse("password does not match.");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
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
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplNicknameException.class)
    public ResponseEntity<BaseErrorResponse> handleException(DuplNicknameException e) {
        BaseErrorResponse response = new BaseErrorResponse("nickname already exists.");
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<BaseErrorResponse> handleException(MemberNotFoundException e) {
        BaseErrorResponse response = new BaseErrorResponse("member not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<BaseErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        BaseErrorResponse response = new BaseErrorResponse("해당 엔티티를 찾을 수 없습니다.");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<BaseErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        BaseErrorResponse response = new BaseErrorResponse("이미 존재하는 데이터입니다.");
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        BaseErrorResponse response = new BaseErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessTokenInvalidException.class)
    public ResponseEntity<BaseErrorResponse> handleAccessDeniedException(AccessTokenInvalidException e) {
        BaseErrorResponse response = new BaseErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Void> handleIoException() {
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        String errorMessage = String.join(" ", errors);
        BaseErrorResponse response = new BaseErrorResponse(errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
