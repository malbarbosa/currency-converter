package com.demo.currencyconverter.controller;

import com.demo.currencyconverter.controller.request.ConversionRequest;
import com.demo.currencyconverter.controller.response.ConversionResponse;
import com.demo.currencyconverter.model.Conversion;
import com.demo.currencyconverter.service.ConversionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
public class ConversionController implements BaseController {

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private ModelMapper mapper;

    @PostMapping(value = "/conversion")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ConversionResponse> convertCurrency(@Valid ConversionRequest request) {
        var conversion = Conversion.getInstance();
        this.mapper.map(conversion,request);
        final Mono<Conversion> convert = conversionService.convert(conversion);
        return getConversionResponse(convert);
    }

    private Mono<ConversionResponse> getConversionResponse(Mono<Conversion> convert) {
        return convert
                .map(conversion -> mapper.map(conversion,ConversionResponse.class))
                .switchIfEmpty(Mono.just(new ConversionResponse()));
    }

    @GetMapping(value = "/conversion/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<ConversionResponse> findAllConversionByUserId(@NotNull String userId) {
         return Flux.just(new ConversionResponse());
    }
}
