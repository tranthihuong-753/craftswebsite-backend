package com.example.demo.exception;

import lombok.Getter;

@Getter 
public class AppException extends RuntimeException {
    private final String errorCode;
    private final int httpStatus;

    public AppException(String errorCode, String message, int httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}