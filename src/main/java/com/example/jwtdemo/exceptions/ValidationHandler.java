package com.example.jwtdemo.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidationHandler {
    // handle validation exceptions
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<String> handleValidationException(GlobalException ex){
        // respond with an error message bad request status code 400 and the exception message
return ResponseEntity.badRequest().body(ex.getMessage());

    }
}
