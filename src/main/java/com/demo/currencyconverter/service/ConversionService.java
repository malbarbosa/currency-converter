package com.demo.currencyconverter.service;

import com.demo.currencyconverter.model.Conversion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ConversionService {
	
	Flux<Conversion> findAllConversionByUserId(final String userId);
	
	Mono<Conversion> convert(final Conversion conversion);

}
