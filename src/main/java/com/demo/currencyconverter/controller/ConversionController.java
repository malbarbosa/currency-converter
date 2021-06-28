package com.demo.currencyconverter.controller;

import com.demo.currencyconverter.controller.request.ConversionRequest;
import com.demo.currencyconverter.controller.response.ConversionResponse;
import com.demo.currencyconverter.controller.response.ErrorResponse;
import com.demo.currencyconverter.exception.EntityNotFoundException;
import com.demo.currencyconverter.model.Conversion;
import com.demo.currencyconverter.service.ConversionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@Log4j2
public class ConversionController implements BaseController {

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private ModelMapper mapper;


    @Operation(summary = "Save a conversion made by a user", operationId = "convertCurrency",
            description = "Endpoint use to create a new conversion",
            tags = "conversion", responses = {
            @ApiResponse(responseCode = "201",
                    description = "successful operation",
                    content = @Content(schema = @Schema(implementation = ConversionResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "bad request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Any rate was found.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "500",
                    description = "The conversion did not go well, try later.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping(value = "/conversions", consumes = "application/json", produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "successful operation")
    public Mono<ConversionResponse> convertCurrency(@Valid @RequestBody ConversionRequest request) {
        log.info("Start method convertCurrency");
        var conversion = Conversion.getInstance();
        setupMapper();
        this.mapper.map(request,conversion);
        return conversionService.convert(conversion)
                .map(conversionMono -> mapper.map(conversionMono, ConversionResponse.class))
                .switchIfEmpty(Mono.error(new EntityNotFoundException("rate.notfound")))
                .log(log.getName());
    }

    private void setupMapper() {
        mapper.typeMap(ConversionRequest.class,Conversion.class).addMappings(configMapper -> configMapper.skip(Conversion::setId));
    }

    @Operation(summary = "Find conversions by userId", operationId = "findAllConversionByUserId",
            description = "Endpoint use to find all conversions by userId",
            tags = "conversion", responses = {
            @ApiResponse(responseCode = "200",
                    description = "successful operation.",
                    content = @Content(schema = @Schema(implementation = ConversionResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "User has no saved conversions.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "500",
                    description = "The conversion did not go well, try later.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping(value = "/conversions/{userId}", consumes = "application/json", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK, reason = "successful operation")
    public Flux<ConversionResponse> findAllConversionByUserId(@PathVariable @NotBlank String userId) {
        log.info("Start method findAllConversionByUserId");
        return conversionService.findAllConversionByUserId(userId)
                .map(conversion ->
                        mapper.map(conversion, ConversionResponse.class)
                ).switchIfEmpty(Mono.just(new ConversionResponse()))
                .log(ConversionController.log.getName());
    }
}
