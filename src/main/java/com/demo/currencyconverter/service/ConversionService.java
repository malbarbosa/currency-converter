package com.demo.currencyconverter.service;

import com.demo.currencyconverter.model.Conversion;
import reactor.core.publisher.Flux;

public interface ConversionService {
	
	Flux<Conversion> findAllConversionByUserId(final String userId);
	
	Flux<Conversion> convert(final Conversion conversion);

}
