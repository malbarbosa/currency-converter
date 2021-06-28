package com.demo.currencyconverter.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Validated
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRequest  implements Serializable {

  @NotBlank
  @Size(max = 255)
  private String name;

  @NotBlank
  @Size(max = 128)
  @Email
  private String email;



}

