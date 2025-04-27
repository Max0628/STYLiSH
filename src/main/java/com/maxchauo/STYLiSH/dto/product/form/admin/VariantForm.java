package com.maxchauo.STYLiSH.dto.product.form.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariantForm {
  private String colorCode;
  private String colorName;
  private String size;
  private String stock;
}
