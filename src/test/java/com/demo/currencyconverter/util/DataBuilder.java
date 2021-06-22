package com.demo.currencyconverter.util;


import com.demo.currencyconverter.model.Conversion;
import com.demo.currencyconverter.model.User;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class DataBuilder {

    public static User createNewUser(){
        User user = new User();
        user.setId("123");
        user.setName("test");
        return user;
    }
}
