package com.phosservice.Phosservice.Exceptions;

import org.springframework.http.HttpStatus;

public class ValidationException extends RuntimeException{
    String message;
    HttpStatus httpStatus;

    ValidationException(String message,HttpStatus httpStatus){
        this.message=message;
        this.httpStatus=httpStatus;
    }
}
