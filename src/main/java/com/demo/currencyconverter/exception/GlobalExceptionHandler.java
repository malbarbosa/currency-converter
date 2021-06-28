package com.demo.currencyconverter.exception;

import com.demo.currencyconverter.controller.response.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.server.EntityResponse;
import reactor.core.publisher.Mono;

import java.util.Locale;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(RestErrorException.class)
    public Mono<EntityResponse<ErrorResponse>> handleError(RuntimeException ex) {
        RestErrorException error = (RestErrorException) ex;
        var errorResponse = ErrorResponse.builder().code(error.getStatus().value()).message(messageSource.getMessage(error.getMessage(),null, Locale.US) ).build();
        return EntityResponse.fromObject(errorResponse).status(error.getStatus()).build();
    }
}
