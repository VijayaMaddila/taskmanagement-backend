package com.taskmanagement.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        String message = ex.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (message != null && (message.contains("not found") || message.contains("Invalid credentials"))) {
            status = HttpStatus.UNAUTHORIZED;
        } else if (message != null && message.contains("already")) {
            status = HttpStatus.CONFLICT;
        }

        return ResponseEntity.status(status).body(Map.of("error", message != null ? message : "An error occurred"));
    }
}
