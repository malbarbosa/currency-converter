package com.demo.currencyconverter.controller.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@Validated
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class UserRequest  implements Serializable {

  @NotBlank
  @Length(max = 255)
  private String name;

  @NotBlank
  @Length(max = 128)
  @Email
  private String email;



}

