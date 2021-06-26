package com.demo.currencyconverter.exception;

import com.demo.currencyconverter.controller.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RestErrorException.class)
    public ResponseEntity<ErrorResponse> handleEntityExists(RuntimeException ex) {
        RestErrorException error = (RestErrorException) ex;
        ErrorResponse errorResponse = ErrorResponse.builder().code(error.getStatus().value()).message(error.getMessage()).build();
        return ResponseEntity.status(error.getStatus()).body(errorResponse);
    }
}
