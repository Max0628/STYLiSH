package com.maxchauo.STYLiSH.controller.product;

import com.maxchauo.STYLiSH.dto.product.form.ProductQueryConditionForm;
import com.maxchauo.STYLiSH.dto.product.dto.ProductResponseDto;
import com.maxchauo.STYLiSH.service.product.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
  private final ProductService service;

  public ProductController(ProductService service) {
    this.service = service;
  }

  @GetMapping("/{category}")
  public ResponseEntity<ProductResponseDto> getProductByCategory(
      @PathVariable String category,
      @RequestParam(defaultValue = "0") int paging,
      @RequestParam(defaultValue = "6") int pageSize) {
    ProductQueryConditionForm condition = new ProductQueryConditionForm();
    condition.setCategory(category);
    condition.setPaging(paging);
    condition.setPageSize(pageSize);
    ProductResponseDto response = service.findProductByCondition(condition);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/search")
  public ResponseEntity<ProductResponseDto> searchProduct(
      @RequestParam(required = true) String keyword,
      @RequestParam(defaultValue = "0") int paging,
      @RequestParam(defaultValue = "6") int pageSize) {
    ProductQueryConditionForm condition = new ProductQueryConditionForm();
    condition.setKeyword(keyword);
    condition.setPaging(paging);
    condition.setPageSize(pageSize);
    ProductResponseDto response = service.findProductByCondition(condition);
    return ResponseEntity.ok(response);
  }

  @GetMapping("details")
  public ResponseEntity<ProductResponseDto> searchProductDetail(
      @RequestParam(required = true) Integer id,
      @RequestParam(defaultValue = "0") int paging,
      @RequestParam(defaultValue = "6") int pageSize) {
    ProductQueryConditionForm condition = new ProductQueryConditionForm();
    condition.setId(id);
    condition.setPaging(paging);
    condition.setPageSize(pageSize);
    ProductResponseDto response = service.findProductByCondition(condition);
    return ResponseEntity.ok(response);
  }
}
