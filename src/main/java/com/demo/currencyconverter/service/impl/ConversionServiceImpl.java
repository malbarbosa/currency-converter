package com.demo.currencyconverter.service.impl;

import com.demo.currencyconverter.dto.CurrencyRateDTO;
import com.demo.currencyconverter.exception.NotFoundException;
import com.demo.currencyconverter.model.Conversion;
import com.demo.currencyconverter.repository.ConversionRepository;
import com.demo.currencyconverter.repository.RateRepository;
import com.demo.currencyconverter.service.ConversionService;
import com.demo.currencyconverter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class ConversionServiceImpl implements ConversionService {

    @Autowired
    private ConversionRepository conversionRepository;

    @Autowired
    private RateRepository rateRepository;

    @Autowired
    private UserService userService;

    @Override
    public Flux<Conversion> findAllConversionByUserId(String userId) {
        return userService.findById(userId)
                .flatMapMany(user -> {
                    if(user != null){
                        return Flux.error(new NotFoundException(1,"User not found"));
                    }else{
                        return conversionRepository.findConversionByUserId(userId);
                    }
                });
    }

    @Override
    public Flux<Conversion> convert(Conversion conversion) {
        final Flux<CurrencyRateDTO> rateFlux = rateRepository.findRate(conversion.getSourceCurrency(), conversion.getTargetCurrency());
        return rateFlux.timeout(Duration.ofSeconds(10))
                .flatMap(rate -> {
                    final Double rateValue = rate.getRates().get(conversion.getTargetCurrency());
                    if(rateValue == null){
                        return Mono.error(new NotFoundException(3,"Any rate was found."));
                    }else{
                        conversion.calculateTargetValue(rateValue);

                        return conversionRepository.save(conversion);
                    }
                });
    }
}
