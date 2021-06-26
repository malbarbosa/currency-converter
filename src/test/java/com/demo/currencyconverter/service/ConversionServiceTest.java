package com.demo.currencyconverter.service;

import com.demo.currencyconverter.dto.CurrencyRateDTO;
import com.demo.currencyconverter.model.Conversion;
import com.demo.currencyconverter.model.User;
import com.demo.currencyconverter.repository.ConversionRepository;
import com.demo.currencyconverter.repository.RateRepository;
import com.demo.currencyconverter.service.impl.ConversionServiceImpl;
import com.demo.currencyconverter.util.DataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ConversionServiceImpl.class)
@ExtendWith(value = MockitoExtension.class)
class ConversionServiceTest {


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
        final Flux<Conversion> conversions = Flux.just(Conversion.of("0001","123","BRL", BigDecimal.valueOf(1),"USD").calculateTargetValue(BigDecimal.valueOf(5)),
                Conversion.of("0001","123","USD",BigDecimal.valueOf(1),"BRL").calculateTargetValue(BigDecimal.valueOf(0.2)));
        when(conversionRepository.findConversionByUserId(any())).thenReturn(conversions);
        final Flux<Conversion> allConversionByUserId = conversionService.findAllConversionByUserId("123");
        StepVerifier.create(allConversionByUserId).expectNextCount(2).verifyComplete();

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
        Map<String, BigDecimal> mapRate = new HashMap<>();
        mapRate.put("USD",BigDecimal.valueOf(5));
        currencyRateDTO.setRates(mapRate);
        when(rateRepository.findRate(anyString(),anyString())).thenReturn(Mono.just(currencyRateDTO));
        final Conversion conversion = Conversion.of(null,"123","BRL",BigDecimal.valueOf(25),"USD");
        final Mono<Conversion> conversionFlux = conversionService.convert(conversion);
        conversion.setId("123");
        when(conversionRepository.save(any(Conversion.class))).thenReturn(Mono.just(conversion));
        StepVerifier.create(conversionFlux)
                .expectNext(conversion)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should not Convert currency with rate invalid")
    void shouldNotConvert() {
        when(rateRepository.findRate(anyString(),anyString())).thenReturn(Mono.just(DataBuilder.currencyRateDTODefault()));
        final Conversion conversion = Conversion.of("0001","123","BRL", BigDecimal.valueOf(25),"EUR");
        final Mono<Conversion> conversionFlux = conversionService.convert(conversion);
        StepVerifier.create(conversionFlux)
                .verifyErrorMessage("Any rate was found.");
    }
}