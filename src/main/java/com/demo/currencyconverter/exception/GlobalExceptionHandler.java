package com.demo.currencyconverter.exception;

import com.demo.currencyconverter.api.model.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RestErrorException.class)
    public ResponseEntity<ErrorResponse> handleEntityExists(RuntimeException ex) {
        RestErrorException error = (RestErrorException) ex;
        ErrorResponse errorResponse = new ErrorResponse().code(error.getStatus().value()).message(error.getMessage());
        return ResponseEntity.status(error.getStatus()).body(errorResponse);
    }
}
