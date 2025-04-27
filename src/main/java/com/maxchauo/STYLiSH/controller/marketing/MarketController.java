package com.maxchauo.STYLiSH.controller.marketing;

import com.maxchauo.STYLiSH.dto.product.dto.ApiResponse;
import com.maxchauo.STYLiSH.dto.product.dto.product.CampaignDto;
import com.maxchauo.STYLiSH.dto.product.dto.product.ProductResponseDto;
import com.maxchauo.STYLiSH.dto.product.dto.wrapper.CampaignFormWrapper;
import com.maxchauo.STYLiSH.dto.product.dto.wrapper.DataWrapper;
import com.maxchauo.STYLiSH.service.market.MarketService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1/marketing")
public class MarketController {
  private final MarketService service;

  public MarketController(MarketService service) {
    this.service = service;
  }

  @GetMapping("/getAllProductIdAndTitle")
  public ResponseEntity<ProductResponseDto> getAllProductIdAndName() {
    ProductResponseDto response = service.getAllProductIdAndTitle();
    return ResponseEntity.ok(response);
  }

  @PostMapping("/insertCampaignProduct")
  public ResponseEntity<ApiResponse> insertCampaignProduct(
      @ModelAttribute CampaignFormWrapper formWrapper) {
    ApiResponse response = service.insertCampaignProduct(formWrapper.getCampaignForms());
    if ("200".equals(response.getStatusCode())) {
      return ResponseEntity.ok(response);
    } else {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  @GetMapping("/campaigns")
  public ResponseEntity getAllCampaignInfo() {
    DataWrapper<List<CampaignDto>> response = service.getAllCampaignInfo();
    if (response.getData() != null) {
      return ResponseEntity.ok(response);
    } else {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("500", "Get all campaign info failed", null));
    }
  }
}
