package com.maxchauo.STYLiSH.repository.market;

import com.maxchauo.STYLiSH.dto.product.dto.product.CampaignDto;
import com.maxchauo.STYLiSH.dto.product.dto.product.ProductResponseDto;
import com.maxchauo.STYLiSH.dto.product.form.admin.CampaignForm;

import java.util.List;


public interface MarketRepository {
  ProductResponseDto getAllProductIdAndTitle();
  boolean insertCampaignProduct(List<CampaignForm> campaignForms);
  List<CampaignDto> getAllCampaignInfo();
}
