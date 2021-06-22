package com.demo.currencyconverter.service.impl;

import com.demo.currencyconverter.dto.CurrencyRateDTO;
import com.demo.currencyconverter.dto.RateDTO;
import com.demo.currencyconverter.model.Conversion;
import com.demo.currencyconverter.model.User;
import com.demo.currencyconverter.repository.ConversionRepository;
import com.demo.currencyconverter.repository.RateRepository;
import com.demo.currencyconverter.service.UserService;
import com.demo.currencyconverter.util.DataBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ConversionServiceImpl.class)
@ExtendWith(value = MockitoExtension.class)
class ConversionServiceImplTest {

    @MockBean
    private ConversionRepository conversionRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private RateRepository rateRepository;

    @InjectMocks
    private ConversionServiceImpl conversionService;

    @Test
    @DisplayName("Find all conversions by user id")
    void findAllConversionByUserId() {
        final User user = DataBuilder.createNewUser();
        when(userService.findById("123")).thenReturn(Mono.just(user));
        final Flux<Conversion> conversions = Flux.just(Conversion.of("0001","123","BRL",1d,"USD").calculateTargetValue(5d),
                Conversion.of("0001","123","USD",1d,"BRL").calculateTargetValue(0.2d));
        when(conversionRepository.findConversionByUserId(any())).thenReturn(conversions);
        final Flux<Conversion> allConversionByUserId = conversionService.findAllConversionByUserId("123");
        assertEquals(2,allConversionByUserId.collectList().block().size());
    }

    @Test
    @DisplayName("Should return error when user id not found")
    void shouldNotFindAnyConversionByUserId() {
        when(userService.findById("123")).thenReturn(Mono.empty());
        conversionService.findAllConversionByUserId("123");
        verify(conversionRepository,times(0)).findConversionByUserId(any());
    }

    @Test
    @DisplayName("Convert currency")
    void convert() {
        CurrencyRateDTO currencyRateDTO = new CurrencyRateDTO();
        currencyRateDTO.setBase("BRL");
        currencyRateDTO.setSuccess("success");
        Map<String, Double> mapRate = new HashMap<>();
        mapRate.put("USD",5d);
        currencyRateDTO.setRates(mapRate);
        when(rateRepository.findRate(anyString(),anyString())).thenReturn(Flux.just(currencyRateDTO));
        final Conversion conversion = Conversion.of(null,"123","BRL",25d,"USD");
        final Flux<Conversion> conversionMono = conversionService.convert(conversion);
        when(conversionRepository.save(any(Conversion.class))).thenReturn(Mono.just(conversion));

        StepVerifier.create(conversionMono).expectComplete().verify();
    }

    @Test
    @DisplayName("Should not Convert currency with rate invalid")
    void shouldNotConvert() {
        when(rateRepository.findRate(anyString(),anyString())).thenReturn(Flux.empty());
        final Conversion conversion = Conversion.of("0001","123","BRL",25d,"USD");
        conversionService.convert(conversion);
        verify(conversionRepository,times(0)).save(any(Conversion.class));
    }
}