package com.demo.currencyconverter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RestErrorException{
    public EntityNotFoundException(){
        super(HttpStatus.NOT_FOUND);
    }
    public EntityNotFoundException(String message){
        super(HttpStatus.NOT_FOUND,message);
    }

}
