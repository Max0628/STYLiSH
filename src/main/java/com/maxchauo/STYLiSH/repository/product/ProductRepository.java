package com.maxchauo.STYLiSH.repository.product;

import com.maxchauo.STYLiSH.dto.product.dto.product.*;
import com.maxchauo.STYLiSH.dto.product.form.admin.CampaignForm;
import com.maxchauo.STYLiSH.dto.product.form.admin.ProductQueryConditionForm;

import java.util.List;
import java.util.Map;

public interface ProductRepository {
  List<ProductDto> findProductByCondition(ProductQueryConditionForm condition);
  Map<Long, List<String>> findImageUrlById(List<Long> productIds);
  Map<Long, List<VariantDto>> findVariantById(List<Long> productIds);
  Map<Long, ColorDto> findColorById(List<Long> colorIds);
  Map<Long, SizeDto> findSizeById(List<Long> sizeIds);
}
