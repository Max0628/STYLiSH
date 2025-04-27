package com.maxchauo.STYLiSH.service.product;

import com.maxchauo.STYLiSH.dto.product.dto.ApiResponse;
import com.maxchauo.STYLiSH.dto.product.dto.wrapper.DataWrapper;
import com.maxchauo.STYLiSH.dto.product.dto.product.CampaignDto;
import com.maxchauo.STYLiSH.dto.product.form.admin.CampaignForm;
import com.maxchauo.STYLiSH.dto.product.form.admin.ProductQueryConditionForm;
import com.maxchauo.STYLiSH.dto.product.dto.product.ProductResponseDto;

import java.util.List;

public interface ProductService {
  ProductResponseDto findProductByCondition(ProductQueryConditionForm condition);
}
