package com.maxchauo.STYLiSH.dto.product.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TapPayResponseDto {
  private int status;
  private String msg;

  @JsonProperty("rec_trade_id")
  private String recTradeId;

  @JsonProperty("bank_transaction_id")
  private String bankTransactionId;

  @JsonProperty("order_number")
  private String orderNumber;

  @JsonProperty("auth_code")
  private String authCode;

  @JsonProperty("amount")
  private int amount;

  private String currency;

  @JsonProperty("transaction_time_millis")
  private long transactionTimeMillis;

  @JsonProperty("card_info")
  private CardInfo cardInfo;

  public static class CardInfo {
    @JsonProperty("bin_code")
    private String binCode;

    @JsonProperty("last_four")
    private String lastFour;

    private String issuer;

    @JsonProperty("issuer_zh_tw")
    private String issuerZhTw;

    @JsonProperty("bank_id")
    private String bankId;

    private int funding; // -1 = Unknown, 0 = Credit, 1 = Debit, 2 = Prepaid
    private int type; // -1 = Unknown, 1 = VISA, etc.
    private String level;
    private String country;

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("expiry_date")
    private String expiryDate;
  }
}
