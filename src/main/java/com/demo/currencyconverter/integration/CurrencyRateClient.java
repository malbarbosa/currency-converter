package com.demo.currencyconverter.integration;

import com.demo.currencyconverter.dto.CurrencyRateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "rateClient", url = "${feign.client.url}")
public interface CurrencyRateClient {
	
	@GetMapping(path = "/latest",  params = {"base","symbols"} ,consumes = "application/json")
	CurrencyRateDTO findRate(@RequestParam(value = "base") String sourceCurrency, @RequestParam(value = "symbols") String targetCurrency);

}
