package com.maxchauo.STYLiSH.dto.product.form.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDataForm {
  private Long userId;

  @JsonProperty("shipping")
  private String shippingMethod;

  @JsonProperty("payment")
  private String paymentMethod;

  private Integer subtotal;
  private Integer freight;
  private Integer total;
  private Integer statusId;
  private RecipientForm recipient;

  @JsonProperty("list")
  private List<OrderItemForm> orderItems;
}
