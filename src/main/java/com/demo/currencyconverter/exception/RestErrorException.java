package com.demo.currencyconverter.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class RestErrorException extends RuntimeException{

    @Getter
    private final HttpStatus status;

    public RestErrorException(HttpStatus status){
        this.status = status;
    }

    public RestErrorException(HttpStatus status, String message){
        super(message);
        this.status = status;
    }


}
