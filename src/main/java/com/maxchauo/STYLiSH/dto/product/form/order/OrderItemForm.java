package com.maxchauo.STYLiSH.dto.product.form.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemForm {
  @JsonProperty("id")
  private Long productId;

  @JsonProperty("name")
  private String productTitleSnapshot;

  @JsonProperty("price")
  private Integer unitPrice;

  private ColorForm color;
  private String size;

  @JsonProperty("qty")
  private Integer quantity;
}
