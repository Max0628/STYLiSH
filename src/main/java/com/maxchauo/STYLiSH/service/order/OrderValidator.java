package com.maxchauo.STYLiSH.service.order;

import com.maxchauo.STYLiSH.dto.product.dto.product.ProductDto;
import com.maxchauo.STYLiSH.dto.product.dto.product.VariantDto;
import com.maxchauo.STYLiSH.dto.product.form.order.OrderItemForm;
import com.maxchauo.STYLiSH.exception.UserClientException;
import com.maxchauo.STYLiSH.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderValidator {

  private final ProductRepository productRepository;

  public void validateOrderItem(OrderItemForm item) {
    Long productId = item.getProductId();

    // Check if productId is null or not exist
    if (productId == null || !productRepository.existsProductById(productId)) {
      throw new UserClientException("product not exist: " + productId);
    }

    // Check if productTitleSnapshot is null or not exist
    if (item.getColor() == null || item.getColor().getCode() == null || item.getColor().getName() == null) {
      throw new UserClientException("orderItem color info missing");
    }

    // Check if colorId is null or not exist
    Long colorId = productRepository.findColorIdByCodeAndName(item.getColor().getCode(), item.getColor().getName());
    if (colorId == null) {throw new UserClientException(
          "color not exist: " + item.getColor().getCode() + "/" + item.getColor().getName());
    }

    if (item.getSize() == null) {
      throw new UserClientException("size is missing");
    }

    Long sizeId = productRepository.findSizeIdBySize(item.getSize());
    if (sizeId == null) {
      throw new UserClientException("size not exist: " + item.getSize());
    }

    // Check if variantId is null or not exist
    VariantDto variant = productRepository.findVariantByProductIdAndColorIdAndSizeId(productId, colorId, sizeId);

    if (variant == null) {
      throw new UserClientException(
          "variant not exist: productId=" + productId + ", colorId=" + colorId + ", sizeId=" + sizeId);
    }

    // Check if stock is enough
    if (item.getQuantity() == null || variant.getStock() < item.getQuantity()) {
      throw new UserClientException("stock not enough or quantity missing, remain: " + variant.getStock());
    }

    ProductDto product = productRepository.findProductById(productId);
    if (product == null || product.getPrice() == null) {
      throw new UserClientException("not product found.");
    }

    Integer dbPrice = Math.toIntExact(product.getPrice());
    Integer inputPrice = item.getUnitPrice();

    if (inputPrice == null || !inputPrice.equals(dbPrice)) {
      throw new UserClientException("wrong unit price, " + dbPrice + " != " + inputPrice);
    }
  }
}
