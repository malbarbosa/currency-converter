package com.demo.currencyconverter.util;



import com.demo.currencyconverter.controller.request.ConversionRequest;
import com.demo.currencyconverter.controller.request.UserRequest;
import com.demo.currencyconverter.controller.response.ConversionResponse;
import com.demo.currencyconverter.controller.response.UserResponse;
import com.demo.currencyconverter.dto.CurrencyRateDTO;
import com.demo.currencyconverter.model.Conversion;
import com.demo.currencyconverter.model.User;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class DataBuilder {

    public static User createNewUser(){
        return new User("123","test","123@test.com");
    }

    public static UserRequest createNewUserRequest(){
        return new UserRequest("123","123@test.com");
    }

    public static UserResponse getUserResponse() {
        return new UserResponse("123","test","123@test.com");
    }

    public static CurrencyRateDTO currencyRateDTODefault(){
        CurrencyRateDTO currencyRateDTO = new CurrencyRateDTO();
        currencyRateDTO.setBase("EUR");
        currencyRateDTO.setSuccess("success");
        Map<String, BigDecimal> mapRate = new HashMap<>();
        mapRate.put("BRL",BigDecimal.valueOf(5.9d));
        mapRate.put("USD",BigDecimal.valueOf(1.19d));
        currencyRateDTO.setRates(mapRate);
        return currencyRateDTO;
    }

    public static ConversionRequest createNewConversionRequest(){
        ConversionRequest request = new ConversionRequest();
        request.setSourceCurrency("BRL");
        request.setTargetCurrency("USD");
        request.setSourceValue(BigDecimal.valueOf(25));
        request.setUserId("123");
        return request;
    }

    public static Conversion createNewConversion(){
        final Conversion conversion = Conversion.of("123456", "123", "BRL", BigDecimal.valueOf(25), "USD");
        return conversion.calculateTargetValue(BigDecimal.valueOf(0.2));
    }

    public static ConversionResponse createNewConversionResponse(){
        ConversionResponse response = new ConversionResponse();
        response.setConversionId("123456");
        response.setSourceCurrency("BRL");
        response.setUserId("123");
        response.setSourceValue(BigDecimal.valueOf(25));
        response.setConversionRate(BigDecimal.valueOf(0.2));
        response.setTargetCurrency("USD");
        response.setTargetValue(BigDecimal.valueOf(5).setScale(2));
        response.setDateTimeConversion(OffsetDateTime.of(2021,1,1,12,0,0,0, ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        return response;
    }
}
