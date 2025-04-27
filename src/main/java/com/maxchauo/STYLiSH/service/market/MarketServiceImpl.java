package com.maxchauo.STYLiSH.service.market;

import com.maxchauo.STYLiSH.dto.product.dto.ApiResponse;
import com.maxchauo.STYLiSH.dto.product.dto.product.CampaignDto;
import com.maxchauo.STYLiSH.dto.product.dto.product.ProductResponseDto;
import com.maxchauo.STYLiSH.dto.product.dto.wrapper.DataWrapper;
import com.maxchauo.STYLiSH.dto.product.form.admin.CampaignForm;
import com.maxchauo.STYLiSH.exception.UserClientException;
import com.maxchauo.STYLiSH.repository.market.MarketRepository;
import com.maxchauo.STYLiSH.util.CommonUtil;
import com.maxchauo.STYLiSH.util.ImgUtil;
import com.maxchauo.STYLiSH.util.JwtUtil;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class MarketServiceImpl implements MarketService {
  @Value("${upload.domain}")
  private String domain;

  @Value("${upload.url-path}")
  private  String urlpath;

  private final MarketRepository repo;
  private final JwtUtil jwtUtil;
  private final ImgUtil imgUtil;

  public MarketServiceImpl(MarketRepository repo, JwtUtil jwtUtil, ImgUtil imgUtil) {
    this.repo = repo;
    this.jwtUtil = jwtUtil;
    this.imgUtil = imgUtil;
  }

  @Override
  public ProductResponseDto getAllProductIdAndTitle() {
    try {
      ProductResponseDto result = repo.getAllProductIdAndTitle();
      return result;
    } catch (Exception e) {
      log.warn("getAllProductIdAndTitle exception: " + e);
    }
    return new ProductResponseDto(List.of(), null);
  }

  @Override
  public ApiResponse insertCampaignProduct(List<CampaignForm> campaignForms) {
    try {
      List<CampaignForm> processedForms = new ArrayList<>();

      for (CampaignForm form : campaignForms) {
        String savedImageName = imgUtil.saveImage(form.getPicture());
        CampaignForm processedForm = new CampaignForm();
        processedForm.setProductId(form.getProductId());
        processedForm.setPictureName(savedImageName);
        processedForm.setStory(form.getStory());
        processedForms.add(processedForm);
      }

      boolean result = repo.insertCampaignProduct(processedForms);

      if (result) {
        return new ApiResponse("200", "Insert campaign product success", null);
      } else {
        return new ApiResponse("500", "Insert campaign product failed", null);
      }
    } catch (Exception e) {
      log.error("insertCampaignProduct exception: ", e);
      throw new UserClientException("user wrong data");
    }
  }

  @Override
  public DataWrapper<List<CampaignDto>> getAllCampaignInfo() {
    try {
      List<CampaignDto> result = repo.getAllCampaignInfo();
      List<CampaignDto> campaignDtos = new ArrayList<>();
      for (CampaignDto item : result) {
        CampaignDto dto = new CampaignDto();
        dto.setProductId(item.getProductId());
        dto.setPicture(CommonUtil.buildFullImageUrl(domain, urlpath, item.getPicture()));
        dto.setStory(item.getStory());
        campaignDtos.add(dto);
      }
      return new DataWrapper<>(campaignDtos);
    } catch (Exception e) {
      log.warn("getAllCampaignInfo exception: ", e);
      throw new UserClientException("wrong url path");
    }
  }
}
