package com.maxchauo.STYLiSH.dto.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColorDto {
  private Long id;
  private String code;
  private String name;
}
