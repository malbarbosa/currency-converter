package com.demo.currencyconverter.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Validated
@Data
public class UserRequest   {
  @JsonProperty("id")
  @NotNull
  private String id = null;

  @JsonProperty("username")
  @NotBlank
  @Max(value = 255)
  private String username = null;


}

