package com.demo.currencyconverter.controller;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(BaseController.BASE_PATH)
public interface BaseController {

    static final String BASE_PATH = "/v1/converter-currency";
}
