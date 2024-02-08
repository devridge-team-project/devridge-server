package org.devridge.api.exception;

import org.devridge.api.exception.common.ForbiddenException;
import org.devridge.api.exception.common.NotFoundException;
import org.devridge.api.exception.common.BadRequestException;
import org.devridge.api.exception.common.UnauthorizeException;
import org.devridge.common.dto.BaseErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 400 BadRequest
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BaseErrorResponse> badRequestHandler(BadRequestException exception) {
        BaseErrorResponse response = new BaseErrorResponse(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 404 NotFound
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public BaseErrorResponse notFoundHandler(NotFoundException exception) {
        return new BaseErrorResponse(exception.getMessage());
    }

    /**
     * 403 Forbidden
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public BaseErrorResponse forbiddenHandler(ForbiddenException exception) {
        return new BaseErrorResponse(exception.getMessage());
    }

    /**
     * 401 Unauthorization
     */
    @ExceptionHandler(UnauthorizeException.class)
    public BaseErrorResponse unauthorizationHandler(UnauthorizeException exception) {
        return new BaseErrorResponse(exception.getMessage());
    }
}
