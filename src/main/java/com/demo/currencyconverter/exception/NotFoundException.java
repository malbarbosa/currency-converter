package com.demo.currencyconverter.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RestErrorException{
    public NotFoundException(){
        super(HttpStatus.NOT_FOUND);
    }
    public NotFoundException(Integer code, String message){
        super(HttpStatus.NOT_FOUND,message);
    }

}
