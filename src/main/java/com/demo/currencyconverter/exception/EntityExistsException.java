package com.demo.currencyconverter.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EntityExistsException extends Exception{

    private Integer code;

    public EntityExistsException(Integer code, String message){
        super(message);
        this.code = code;
    }

}
