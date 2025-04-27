package com.maxchauo.STYLiSH.dto.product.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariantOutputDto {
  @JsonProperty("color_code")
  private String colorCode;

  private String size;
  private Long stock;
}
