package com.maxchauo.STYLiSH.dto.product.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VariantForm {
  private String colorCode;
  private String colorName;
  private String size;
  private String stock;
}
