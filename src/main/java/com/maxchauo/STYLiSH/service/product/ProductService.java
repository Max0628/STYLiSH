package com.maxchauo.STYLiSH.service.product;

import com.maxchauo.STYLiSH.dto.product.form.ProductQueryConditionForm;
import com.maxchauo.STYLiSH.dto.product.dto.ProductResponseDto;

public interface ProductService {
  ProductResponseDto findProductByCondition(ProductQueryConditionForm condition);
}
