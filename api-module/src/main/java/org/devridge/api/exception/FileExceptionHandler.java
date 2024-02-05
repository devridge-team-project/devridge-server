package org.devridge.api.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class FileExceptionHandler {

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Void> handleIoException() {
        return ResponseEntity.internalServerError().build();
    }
}
