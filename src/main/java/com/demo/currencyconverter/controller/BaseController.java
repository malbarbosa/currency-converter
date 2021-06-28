package com.demo.currencyconverter.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

@OpenAPIDefinition(info = @Info(
        title = "Currency-converter API",
        version = "1.0.0",
        description = "API to converter one currency to another",
        contact = @Contact(email = "malbarbosa@gmail.com")),
        tags = {
                @Tag(name="user", description="Operations about user"),
                @Tag(name="conversion", description="Operations about conversion currency"),
        }

)
@RequestMapping( value = "/v1/converter-currency")
public interface BaseController {



}
