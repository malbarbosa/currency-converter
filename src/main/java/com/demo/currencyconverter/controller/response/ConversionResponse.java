package com.demo.currencyconverter.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Validated
@Data
public class ConversionResponse  implements Serializable {
  @JsonProperty("conversionId")
  @NotNull
  private String conversionId = null;

  @JsonProperty("userId")
  @NotNull
  private String userId = null;

  @JsonProperty("sourceCurrency")
  @NotBlank
  private String sourceCurrency = null;

  @JsonProperty("sourceValue")
  @NotNull
  private BigDecimal sourceValue = BigDecimal.ZERO;

  @JsonProperty("targetCurrency")
  @NotBlank
  private String targetCurrency = null;

  @JsonProperty("targetValue")
  @NotNull
  private BigDecimal targetValue = BigDecimal.ZERO;

  @JsonProperty("conversionRate")
  @NotNull
  private BigDecimal conversionRate = BigDecimal.ZERO;

  @JsonProperty("dateTimeConversion")
  @NotBlank
  private String dateTimeConversion = null;


}

