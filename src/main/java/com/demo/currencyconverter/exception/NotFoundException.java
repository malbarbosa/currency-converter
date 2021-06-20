package com.demo.currencyconverter.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotFoundException extends Exception{

    private Integer code;

    public NotFoundException(Integer code, String message){
        super(message);
        this.code = code;
    }

}
