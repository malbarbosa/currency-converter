package com.demo.currencyconverter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends RestErrorException {

    public InternalServerErrorException(){
        super(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    public InternalServerErrorException(String message){
        super(HttpStatus.INTERNAL_SERVER_ERROR,message);
    }

}
