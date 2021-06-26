package com.demo.currencyconverter.repository;

import com.demo.currencyconverter.config.RateConfiguration;
import com.demo.currencyconverter.dto.CurrencyRateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@FeignClient(name = "rateClient", url = "${feign.client.url}", configuration = RateConfiguration.class)
public interface RateRepository {
	
	@GetMapping(value = "/latest?base={sourceCurrency}&symbols={targetCurrency}", consumes = "application/json")
	Mono<CurrencyRateDTO> findRate(String sourceCurrency, String targetCurrency);

}
