package com.demo.currencyconverter.service.impl;

import com.demo.currencyconverter.dto.CurrencyRateDTO;
import com.demo.currencyconverter.exception.EntityNotFoundException;
import com.demo.currencyconverter.exception.InternalServerErrorException;
import com.demo.currencyconverter.integration.CurrencyRateClient;
import com.demo.currencyconverter.model.Conversion;
import com.demo.currencyconverter.repository.ConversionRepository;
import com.demo.currencyconverter.service.ConversionService;
import com.demo.currencyconverter.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@Log4j2
public class ConversionServiceImpl implements ConversionService {

    @Autowired
    private ConversionRepository conversionRepository;

    @Autowired
    private CurrencyRateClient currencyRateClient;

    @Autowired
    private UserService userService;

    @Override
    public Flux<Conversion> findAllConversionByUserId(String userId) {
        log.info(String.format("Start method findAllConversionByUserId, userId=%s",userId));
        return userService.findById(userId)
                .flatMapMany(user ->
                        conversionRepository.findConversionByUserId(user.getId()))
                .switchIfEmpty(Flux.error(new EntityNotFoundException("user.without.conversions")));

    }

    @Override
    public Mono<Conversion> convert(Conversion conversion) {
        log.info(String.format("Start method convert, conversion=%s",conversion.toString()));
         final Mono<CurrencyRateDTO> rateMono = Mono.just(currencyRateClient.findRate(conversion.getSourceCurrency(), conversion.getTargetCurrency()));
         return rateMono
                .flatMap(rate -> {
                    Mono<Conversion> conversionMono = null;
                    final BigDecimal rateValue = rate.getRates().get(conversion.getTargetCurrency());
                    if(rateValue == null){
                        log.info(String.format("Rate %s not found.",conversion.getTargetCurrency()));
                        conversionMono = Mono.empty();
                    }else{
                        conversion.calculateTargetValue(rateValue);
                        conversionMono = conversionRepository.save(conversion);
                        log.info("Conversion save with success.");

                    }
                    log.info("Finish method convert.");
                    return conversionMono;
                }).log(log.getName())
                 .doOnError(throwable -> Mono.error(new InternalServerErrorException("conversion.error")));
    }
}
