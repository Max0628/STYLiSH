package com.maxchauo.STYLiSH.dto.product.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariantDto {
  private Long size_id;
  private Long color_id;
  private Long product_id;
  private Long stock;
}
