package com.demo.currencyconverter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class EntityExistsException extends RestErrorException {

    public EntityExistsException(){
        super(HttpStatus.UNPROCESSABLE_ENTITY);
    }
    public EntityExistsException(String message){
        super(HttpStatus.UNPROCESSABLE_ENTITY,message);
    }
}
