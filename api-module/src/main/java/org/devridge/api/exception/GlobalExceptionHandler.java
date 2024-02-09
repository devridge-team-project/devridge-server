package org.devridge.api.exception;

import org.devridge.api.exception.common.BaseException;
import org.devridge.common.dto.BaseErrorResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

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
        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        String errorMessage = String.join(" ", errors);

        return ResponseEntity
            .status(400)
            .body(new BaseErrorResponse(errorMessage));
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