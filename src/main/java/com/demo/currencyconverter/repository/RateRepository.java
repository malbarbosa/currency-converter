package com.demo.currencyconverter.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.demo.currencyconverter.dto.CurrencyRateDTO;

import reactor.core.publisher.Flux;

@FeignClient(name = "rateClient", url = "${feign.url}")
public interface RateRepository {
	
	@RequestMapping(method = RequestMethod.GET, value = "/latest?base={sourceCurrency}&symbols={targetCurrency}", consumes = "application/json")
	Flux<CurrencyRateDTO> findRate(String sourceCurrency, String targetCurrency);

}
