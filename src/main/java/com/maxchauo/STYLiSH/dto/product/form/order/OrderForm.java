package com.maxchauo.STYLiSH.dto.product.form.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderForm {
  private String prime;
  private OrderDataForm order;
}
