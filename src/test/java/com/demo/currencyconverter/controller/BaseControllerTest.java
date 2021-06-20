package com.demo.currencyconverter.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
class BaseControllerTest {

    static final String BASE_PATH = "/v1/converter-currency";
    @Autowired
    public WebTestClient webTestClient;



}