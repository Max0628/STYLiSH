package com.maxchauo.STYLiSH.dto.product.form.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TapPayRequestForm {
  private String prime;

  @JsonProperty("partner_key")
  private String partnerKey;

  @JsonProperty("merchant_id")
  private String merchantId;

  private int amount;
  private String details;
  private Cardholder cardholder;

  @Data
  public static class Cardholder {
    @JsonProperty("phone_number")
    private String phoneNumber;

    private String name;
    private String email;

    @JsonProperty("zip_code")
    private String zipCode;

    private String address;
  }
}
