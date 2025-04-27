package com.maxchauo.STYLiSH.dto.product.form.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInForm {
  private String provider;
  private String email;
  private String password;

  @JsonProperty("access_token")
  private String accessToken;
}
