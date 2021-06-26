package com.demo.currencyconverter.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

/**
 * UserResponse
 */
@Validated
@Data
public class UserResponse   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("name")
  private String name = null;

}

