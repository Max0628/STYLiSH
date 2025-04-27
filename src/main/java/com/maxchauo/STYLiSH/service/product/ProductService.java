package com.maxchauo.STYLiSH.service.product;

import com.maxchauo.STYLiSH.dto.product.form.admin.ProductQueryConditionForm;
import com.maxchauo.STYLiSH.dto.product.dto.product.ProductResponseDto;

public interface ProductService {
  ProductResponseDto findProductByCondition(ProductQueryConditionForm condition);
}
