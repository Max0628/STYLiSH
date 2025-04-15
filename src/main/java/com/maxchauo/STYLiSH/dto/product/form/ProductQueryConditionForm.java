package com.maxchauo.STYLiSH.dto.product.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductQueryConditionForm {
  private Integer id;
  private String keyword;
  private String category;
  private Integer paging;
  private Integer pageSize;
}
