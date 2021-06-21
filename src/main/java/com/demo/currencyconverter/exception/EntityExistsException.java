package com.demo.currencyconverter.exception;

import org.springframework.http.HttpStatus;

public class EntityExistsException extends RestErrorException {
    public EntityExistsException(){
        super(HttpStatus.UNPROCESSABLE_ENTITY);
    }
    public EntityExistsException(Integer code, String message){
        super(HttpStatus.UNPROCESSABLE_ENTITY,message);
    }

}
