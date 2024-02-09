package org.devridge.api.exception;

import org.devridge.api.exception.common.BaseException;
import org.devridge.common.dto.BaseErrorResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * developer custom exception
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseErrorResponse> handleBaseException(BaseException exception) {
        return ResponseEntity
            .status(exception.getCode())
            .body(new BaseErrorResponse(exception.getMessage()));
    }

    /**
     * request valid exception
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseErrorResponse> handleRequestValidException(MethodArgumentNotValidException exception) {
        return ResponseEntity
            .status(400)
            .body(new BaseErrorResponse(exception.getMessage()));
    }

    /**
     * enum validate exception
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity
            .status(400)
            .body(new BaseErrorResponse(exception.getMessage()));
    }
}