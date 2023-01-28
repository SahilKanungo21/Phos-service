package com.phosservice.Phosservice.Exceptions;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
    String message;
    HttpStatus statusCode;

    public CustomException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.statusCode = httpStatus;
    }
}
