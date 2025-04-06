package com.maxchauo.STYLiSH.service.product;

import com.maxchauo.STYLiSH.dto.product.form.ProductQueryCondition;
import com.maxchauo.STYLiSH.dto.product.response.ProductResponse;

public interface ProductService {
  ProductResponse findProductByCondition(ProductQueryCondition condition);
}
