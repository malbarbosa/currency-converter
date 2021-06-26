package com.demo.currencyconverter.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Validated
@Data
public class ConversionRequest   {
  @JsonProperty("userId")
  @NotNull
  private String userId = null;

  @JsonProperty("sourceCurrency")
  @ApiParam(value = "sourceCurrency", required = true)
  @NotBlank
  @Min(value = 3)
  @Max(value = 3)
  @Length(min = 3,max = 3, message = "Currency should have 3 chars. Example: USD, BRL, EUR and so on")
  private String sourceCurrency = null;

  @JsonProperty("sourceValue")
  @ApiParam(value = "sourceValue", required = true)
  private BigDecimal sourceValue = null;

  @JsonProperty("targetCurrency")
  @NotBlank
  @Min(value = 3)
  @Max(value = 3)
  @Length(min = 3,max = 3, message = "Currency should have 3 chars. Example: USD, BRL, EUR and so on")
  @ApiParam(value = "targetCurrency", required = true)
  private String targetCurrency = null;
}

