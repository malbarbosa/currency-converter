package com.demo.currencyconverter.controller;

import com.demo.currencyconverter.controller.request.ConversionRequest;
import com.demo.currencyconverter.controller.response.ConversionResponse;
import com.demo.currencyconverter.exception.InternalServerErrorException;
import com.demo.currencyconverter.exception.NotFoundException;
import com.demo.currencyconverter.model.Conversion;
import com.demo.currencyconverter.service.ConversionService;
import com.demo.currencyconverter.util.DataBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(ConversionController.class)
class ConversionControllerTest extends BaseControllerTest{

    @MockBean
    private ConversionService conversionService;

    @Test
    @DisplayName("Should return 201 when POST /conversion pass data by body")
    void shouldConvertCurrency() {
        Conversion conversion = DataBuilder.createNewConversion();
        Mockito.when(conversionService.convert(Mockito.any(Conversion.class))).thenReturn(Mono.just(conversion));
        final ConversionResponse response = DataBuilder.createNewConversionResponse();
        final WebTestClient.BodySpec<ConversionResponse, ?> value = webTestClient
                .post().uri(BASE_PATH+"/conversion")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(DataBuilder.createNewConversionRequest())
                .exchange().expectStatus().isCreated()
                .expectBody(ConversionResponse.class)
                .value(conversionResponse -> {
                    Assertions.assertEquals(response.getTargetValue(),conversionResponse.getTargetValue());
                });

    }

    @Test
    @DisplayName("Should return 400 when POST /conversion and pass invalid data by body")
    void shouldReturn400AndNotConvertCurrency() {
        Conversion conversion = DataBuilder.createNewConversion();
        Mockito.when(conversionService.convert(Mockito.any(Conversion.class))).thenReturn(Mono.just(conversion));
        final WebTestClient.BodySpec<ConversionResponse, ?> value = webTestClient
                .post().uri(BASE_PATH+"/conversion")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new ConversionRequest())
                .exchange().expectStatus().isBadRequest()
                .expectBody(ConversionResponse.class);
    }

    @Test
    @DisplayName("Should return 500 when POST /conversion and some error occurred")
    void shouldReturn500AndNotConvertCurrency() {
        Mockito.when(conversionService.convert(Mockito.any(Conversion.class))).thenThrow(new InternalServerErrorException());
        final WebTestClient.BodySpec<ConversionResponse, ?> value = webTestClient
                .post().uri(BASE_PATH+"/conversion")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(DataBuilder.createNewConversionRequest())
                .exchange().expectStatus().is5xxServerError()
                .expectBody(ConversionResponse.class);
    }

    @Test
    @DisplayName("Should return 404 when POST /conversion and the currency was not found")
    void shouldReturn404AndNotConvertCurrency() {
        Mockito.when(conversionService.convert(Mockito.any(Conversion.class))).thenThrow(new NotFoundException());
        final WebTestClient.BodySpec<ConversionResponse, ?> value = webTestClient
                .post().uri(BASE_PATH+"/conversion")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(DataBuilder.createNewConversionRequest())
                .exchange().expectStatus().isNotFound()
                .expectBody(ConversionResponse.class);
    }




    @Test
    @DisplayName("Should return all conversion made by one user")
    void findAllConversionByUserId() {
        final WebTestClient.BodySpec<ConversionResponse, ?> value = webTestClient
                .get().uri(BASE_PATH+"/conversion/123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectBody(ConversionResponse.class);
    }
}