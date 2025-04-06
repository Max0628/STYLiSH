package com.maxchauo.STYLiSH.repository.product;

import com.maxchauo.STYLiSH.dto.product.dto.*;
import com.maxchauo.STYLiSH.dto.product.form.ProductQueryCondition;

import java.util.List;
import java.util.Map;

public interface ProductRepository {
  // get productId List.
  List<ProductDto> findProductByCondition(ProductQueryCondition condition);

  // get image map.
  Map<Long, List<String>> findImageUrlById(List<Long> productIds);

  // use productId to get variantList and use variantList extract sizeId & colorId.
  Map<Long, List<VariantDto>> findVariantById(List<Long> productIds);

  // get color and size info by their id.
  Map<Long,ColorDto> findColorById(List<Long> colorIds);
  Map<Long, SizeDto> findSizeById(List<Long> sizeIds);
}
