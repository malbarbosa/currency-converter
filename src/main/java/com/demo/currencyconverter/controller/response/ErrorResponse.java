package com.demo.currencyconverter.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

@Validated
@Data
@Builder
public class ErrorResponse  implements Serializable {
  @JsonProperty("code")
  private Integer code = null;

  @JsonProperty("message")
  private String message = null;

}

