package com.maxchauo.STYLiSH.service.market;

import com.maxchauo.STYLiSH.dto.product.dto.ApiResponse;
import com.maxchauo.STYLiSH.dto.product.dto.product.CampaignDto;
import com.maxchauo.STYLiSH.dto.product.dto.product.ProductResponseDto;
import com.maxchauo.STYLiSH.dto.product.dto.wrapper.DataWrapper;
import com.maxchauo.STYLiSH.dto.product.form.admin.CampaignForm;

import java.util.List;

public interface MarketService {
  ProductResponseDto getAllProductIdAndTitle();
  ApiResponse insertCampaignProduct(List<CampaignForm> campaignForms);
  DataWrapper<List<CampaignDto>> getAllCampaignInfo();
}
