package com.demo.currencyconverter.decoder;

import feign.Response;
import feign.codec.ErrorDecoder;

public class DefaultErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        return errorDecoder.decode(methodKey,response);
    }
}
