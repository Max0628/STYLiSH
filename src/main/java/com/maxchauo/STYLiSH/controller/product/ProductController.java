package com.maxchauo.STYLiSH.controller.product;

import com.maxchauo.STYLiSH.dto.product.form.ProductQueryCondition;
import com.maxchauo.STYLiSH.dto.product.response.ProductResponse;
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
  public ResponseEntity<ProductResponse> getProductByCategory(
          @PathVariable String category,
          @RequestParam(defaultValue = "0") int paging,
          @RequestParam(defaultValue = "6") int pageSize
  ){
    ProductQueryCondition condition = new ProductQueryCondition();
    condition.setCategory(category);
    condition.setPaging(paging);
    condition.setPageSize(pageSize);
    ProductResponse response = service.findProductByCondition(condition);
    return ResponseEntity.ok(response);
  }

}
