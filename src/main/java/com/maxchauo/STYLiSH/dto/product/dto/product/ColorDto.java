package com.maxchauo.STYLiSH.dto.product.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColorDto {
  @JsonIgnore
  private Long id;

  private String code;
  private String name;
}
