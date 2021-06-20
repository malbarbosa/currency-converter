package com.demo.currencyconverter.util;


import com.demo.currencyconverter.model.User;

public class DataBuilder {

    public static User createNewUser(){
        User user = new User();
        user.setId(1L);
        user.setName("test");
        return user;
    }
}
