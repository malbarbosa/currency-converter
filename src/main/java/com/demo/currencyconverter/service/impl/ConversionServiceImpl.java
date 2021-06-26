package com.demo.currencyconverter.service.impl;

import com.demo.currencyconverter.dto.CurrencyRateDTO;
import com.demo.currencyconverter.exception.InternalServerErrorException;
import com.demo.currencyconverter.exception.NotFoundException;
import com.demo.currencyconverter.model.Conversion;
import com.demo.currencyconverter.repository.ConversionRepository;
import com.demo.currencyconverter.repository.RateRepository;
import com.demo.currencyconverter.service.ConversionService;
import com.demo.currencyconverter.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.logging.Logger;

@Service
@Log4j2
public class ConversionServiceImpl implements ConversionService {

    @Autowired
    private ConversionRepository conversionRepository;

    @Autowired
    private RateRepository rateRepository;

    @Autowired
    private UserService userService;

    @Override
    public Flux<Conversion> findAllConversionByUserId(String userId) {
        log.info("Start method findAllConversionByUserId");
        return userService.findById(userId)
                .flatMapMany(user ->
                    conversionRepository.findConversionByUserId(user.getId()))
                .switchIfEmpty(Flux.error(new NotFoundException(1,"User not found"))).log();

    }

    @Override
    public Mono<Conversion> convert(Conversion conversion) {
        final Mono<CurrencyRateDTO> rateFlux = rateRepository.findRate(conversion.getSourceCurrency(), conversion.getTargetCurrency());
        return rateFlux.timeout(Duration.ofSeconds(10))
                .flatMap(rate -> {
                    final BigDecimal rateValue = rate.getRates().get(conversion.getTargetCurrency());
                    if(rateValue == null){
                        return Mono.error(new NotFoundException(3,"Any rate was found."));
                    }else{
                        conversion.calculateTargetValue(rateValue);
                        return conversionRepository.save(conversion);
                    }
                }).doOnError(throwable -> Mono.error(new InternalServerErrorException(9,"The conversion did not go well, try later.")));
    }
}
