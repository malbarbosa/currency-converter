package com.demo.currencyconverter.exception;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends RestErrorException {
    public InternalServerErrorException(){
        super(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    public InternalServerErrorException(Integer code, String message){
        super(HttpStatus.INTERNAL_SERVER_ERROR,message);
    }

}
