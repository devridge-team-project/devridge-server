package org.flow.exception;

import org.devridge.dto.BaseResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<BaseResponse> handleElementNotFountException(NoSuchElementException e) {
        BaseResponse response = new BaseResponse(HttpStatus.NOT_FOUND.value(), "해당 요소를 찾을 수 없습니다.");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
