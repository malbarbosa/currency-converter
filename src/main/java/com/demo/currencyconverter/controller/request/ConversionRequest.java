package com.demo.currencyconverter.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;


@Validated
@Data
public class ConversionRequest  implements Serializable {

  @NotNull
  private String userId = null;

  @NotBlank
  @Size(min = 3,max = 3, message = "Currency should have 3 chars. Example: USD, BRL, EUR, JPY and so on")
  private String sourceCurrency = null;

  @JsonProperty(value = "sourceValue",required = true)
  @NotNull(message = "SourceValue is required")
  private BigDecimal sourceValue = null;

  @JsonProperty("targetCurrency")
  @NotBlank
  @Size(min = 3,max = 3, message = "Currency should have 3 chars. Example: USD, BRL, EUR, JPY and so on")
  private String targetCurrency = null;
}

