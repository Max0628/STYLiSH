package com.maxchauo.STYLiSH.service.market;

import com.fasterxml.jackson.core.type.TypeReference;
import com.maxchauo.STYLiSH.dto.product.dto.ApiResponse;
import com.maxchauo.STYLiSH.dto.product.dto.product.CampaignDto;
import com.maxchauo.STYLiSH.dto.product.dto.product.ProductResponseDto;
import com.maxchauo.STYLiSH.dto.product.dto.wrapper.DataWrapper;
import com.maxchauo.STYLiSH.dto.product.form.admin.CampaignForm;
import com.maxchauo.STYLiSH.exception.UserClientException;
import com.maxchauo.STYLiSH.repository.market.MarketRepository;
import com.maxchauo.STYLiSH.repository.redis.RedisRepository;
import com.maxchauo.STYLiSH.util.CommonUtil;
import com.maxchauo.STYLiSH.util.ImgUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class MarketServiceImpl implements MarketService {
  @Value("${upload.domain}")
  private String domain;

  @Value("${upload.url-path}")
  private String urlPath;

  @Value("${spring.data.campaign.cache.key}")
  private String campaignCacheKey;

  @Value("${spring.data.redis.timeout}")
  private Duration cacheTtlSeconds;

  private final ImgUtil imgUtil;
  private final MarketRepository repo;
  private final RedisRepository redisRepository;

  public MarketServiceImpl(MarketRepository repo, ImgUtil imgUtil, RedisRepository redisRepository) {
    this.repo = repo;
    this.imgUtil = imgUtil;
    this.redisRepository = redisRepository;
  }

  @Override
  public ProductResponseDto getAllProductIdAndTitle() {
    try {
      return repo.getAllProductIdAndTitle();
    } catch (Exception e) {
      log.warn("getAllProductIdAndTitle exception");
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
        // Clear the cache after inserting new campaign products
        redisRepository.delete(campaignCacheKey);
        log.info("Campaign cache cleared after successful insert");
        return new ApiResponse("200", "Insert campaign product success", null);
      } else {
        return new ApiResponse("500", "Insert campaign product failed", null);
      }
    } catch (Exception e) {
      log.error("insertCampaignProduct exception");
      throw new UserClientException("user wrong data");
    }
  }

  @Override
  public DataWrapper<List<CampaignDto>> getAllCampaignInfo() {
    try {
      TypeReference<List<CampaignDto>> typeRef = new TypeReference<>() {};
      // fetch from cache at first
      List<CampaignDto> cachedResult = redisRepository.get(campaignCacheKey, typeRef);
      log.warn("cachedResult: {}", cachedResult);
      if (cachedResult != null && !cachedResult.isEmpty()) {
        log.info("returning cached campaign data");
        return new DataWrapper<>(cachedResult);
      }

    } catch (RedisConnectionFailureException e) {
      log.warn("Redis unavailable. Fallback to DB. cacheKey={}", campaignCacheKey);
    } catch (Exception e) {
      log.warn("Unexpected Redis error while reading cache");
    }

    try {
      // if cache is empty, fetch from database
      List<CampaignDto> result = repo.getAllCampaignInfo();
      log.info("Fetched Campaign Data from Database: " + result.toString());
      List<CampaignDto> campaignDtos = new ArrayList<>();
      for (CampaignDto item : result) {
        CampaignDto dto = new CampaignDto();
        dto.setProductId(item.getProductId());
        dto.setPicture(CommonUtil.buildFullImageUrl(domain, urlPath, item.getPicture()));
        dto.setStory(item.getStory());
        campaignDtos.add(dto);
      }
      try {
        // save to cache
        if (!campaignDtos.isEmpty()) {
          redisRepository.save(campaignCacheKey, campaignDtos, cacheTtlSeconds);
        }
      } catch (RedisConnectionFailureException e) {
        log.warn("Redis unavailable. Skip saving campaign cache");
      }
      return new DataWrapper<>(campaignDtos);
    } catch (Exception e) {
      log.warn("getAllCampaignInfo exception: ");
      throw new UserClientException("wrong url path");
    }
  }
}
