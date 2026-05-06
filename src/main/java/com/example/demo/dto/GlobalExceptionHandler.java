package com.example.demo.dto;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.exception.AppException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<String>> handleApp(AppException ex) {
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(new ApiResponse<>(
                        ex.getErrorCode(),
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleOther(Exception ex) {
        return ResponseEntity
                .status(500)
                .body(new ApiResponse<>(
                        "INTERNAL_ERROR",
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }
}
