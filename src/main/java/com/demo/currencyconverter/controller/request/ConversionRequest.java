package com.demo.currencyconverter.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;


@Validated
@Data
public class ConversionRequest  implements Serializable {

  @NotNull
  private String userId = null;

  @ApiParam(value = "sourceCurrency", required = true)
  @NotBlank
  @Length(min = 3,max = 3, message = "Currency should have 3 chars. Example: USD, BRL, EUR, JPY and so on")
  private String sourceCurrency = null;

  @JsonProperty(value = "sourceValue",required = true)
  @ApiParam(value = "sourceValue", required = true, example = "1.0,5,5.5")
  @NotNull(message = "SourceValue is required")
  private BigDecimal sourceValue = null;

  @JsonProperty("targetCurrency")
  @NotBlank
  @Length(min = 3,max = 3, message = "Currency should have 3 chars. Example: USD, BRL, EUR, JPY and so on")
  @ApiParam(value = "targetCurrency", required = true)
  private String targetCurrency = null;
}

