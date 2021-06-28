package com.demo.currencyconverter.controller;

import com.demo.currencyconverter.controller.request.ConversionRequest;
import com.demo.currencyconverter.controller.response.ConversionResponse;
import com.demo.currencyconverter.exception.EntityNotFoundException;
import com.demo.currencyconverter.model.Conversion;
import com.demo.currencyconverter.service.ConversionService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@Log4j2
public class ConversionController implements BaseController {

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private ModelMapper mapper;

    @PostMapping(value = "/conversions")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "successful operation")
    public Mono<ConversionResponse> convertCurrency(@Valid @RequestBody ConversionRequest request) {
        log.info("Start method convertCurrency");
        var conversion = Conversion.getInstance();
        setupMapper();
        this.mapper.map(request,conversion);
        final Mono<ConversionResponse> responseMono = conversionService.convert(conversion)
                .map(conversionMono -> mapper.map(conversionMono, ConversionResponse.class))
                .switchIfEmpty(Mono.error(new EntityNotFoundException("rate.notfound")))
                .log(log.getName());
        log.info("Finish method convertCurrency");
        return responseMono;
    }

    private void setupMapper() {
        mapper.typeMap(ConversionRequest.class,Conversion.class).addMappings(configMapper -> configMapper.skip(Conversion::setId));
    }

    @GetMapping(value = "/conversions/{userId}")
    @ResponseStatus(value = HttpStatus.OK, reason = "successful operation")
    public Flux<ConversionResponse> findAllConversionByUserId(@PathVariable @NotBlank String userId) {
        log.info("Start method findAllConversionByUserId");
        final Flux<ConversionResponse> conversionResponse = conversionService.findAllConversionByUserId(userId)
                .map(conversion ->
                        mapper.map(conversion, ConversionResponse.class)
                ).switchIfEmpty(Mono.just(new ConversionResponse()))
                .log(ConversionController.log.getName());
        log.info("Finish method findAllConversionByUserId");
        return conversionResponse;
    }
}
