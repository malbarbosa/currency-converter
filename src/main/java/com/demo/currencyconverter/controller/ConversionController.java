package com.demo.currencyconverter.controller;

import com.demo.currencyconverter.controller.request.ConversionRequest;
import com.demo.currencyconverter.controller.response.ConversionResponse;
import com.demo.currencyconverter.exception.EntityNotFoundException;
import com.demo.currencyconverter.model.Conversion;
import com.demo.currencyconverter.service.ConversionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
public class ConversionController implements BaseController {

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private ModelMapper mapper;

    @PostMapping(value = "/conversions")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ConversionResponse> convertCurrency(@Valid @RequestBody ConversionRequest request) {
        var conversion = Conversion.getInstance();
        setupMapper();
        this.mapper.map(request,conversion);
        return  conversionService.convert(conversion)
                .map(conversionMono -> mapper.map(conversionMono,ConversionResponse.class))
                .switchIfEmpty(Mono.error(new EntityNotFoundException("rate.notfound")));

    }

    private void setupMapper() {
        mapper.typeMap(ConversionRequest.class,Conversion.class).addMappings(configMapper -> configMapper.skip(Conversion::setId));
    }

    @GetMapping(value = "/conversions/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<ConversionResponse> findAllConversionByUserId(@PathVariable @NotBlank String userId) {
        return conversionService.findAllConversionByUserId(userId)
                .map(conversion ->
                    mapper.map(conversion, ConversionResponse.class)
                 ).switchIfEmpty(Mono.just(new ConversionResponse()));
    }
}
