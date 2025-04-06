package com.maxchauo.STYLiSH.dto.product.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductQueryCondition {
  private String category;
  private Integer paging;
  private Integer pageSize;
}
