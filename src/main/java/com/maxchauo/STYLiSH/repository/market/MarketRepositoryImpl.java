package com.maxchauo.STYLiSH.repository.market;

import com.maxchauo.STYLiSH.dto.product.dto.product.CampaignDto;
import com.maxchauo.STYLiSH.dto.product.dto.product.ProductDto;
import com.maxchauo.STYLiSH.dto.product.dto.product.ProductResponseDto;
import com.maxchauo.STYLiSH.dto.product.form.admin.CampaignForm;
import com.maxchauo.STYLiSH.exception.DatabaseOperationException;
import com.maxchauo.STYLiSH.mapper.product.CampaignRowMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
@Repository
public class MarketRepositoryImpl implements MarketRepository{
  private final NamedParameterJdbcTemplate template;

  public MarketRepositoryImpl(NamedParameterJdbcTemplate template) {
    this.template = template;
  }

  @Override
  public ProductResponseDto getAllProductIdAndTitle() {
    String QUERY_ALL_PRODUCT_ID_AND_NAME = "SELECT id, title FROM `Product`";
    try {
      List<ProductDto> result = template.query(QUERY_ALL_PRODUCT_ID_AND_NAME, new MapSqlParameterSource(),
              (rs, rowNum) ->
                      new ProductDto(
                              rs.getLong("id"), // id
                              null, // category
                              rs.getString("title"), // title
                              null, // description
                              null, // price
                              null, // texture
                              null, // wash
                              null, // place
                              null, // note
                              null, // story
                              null, // colors
                              null, // sizes
                              null, // variants
                              null, // mainImage
                              null // images
                      ));

      return new ProductResponseDto(result, null);
    } catch (Exception e) {
      log.warn("getAllProductIdAndName exception: ", e);
      return new ProductResponseDto(Collections.emptyList(), null);
    }
  }

  @Override
  public boolean insertCampaignProduct(List<CampaignForm> campaignForms) {
    String INSERT_CAMPAIGN_PRODUCT = "INSERT INTO `Campaign` (product_id, picture, story) VALUES (:productId, :picture, :story)";
    try {
      List<MapSqlParameterSource> batchParams = new ArrayList<>();
      for (CampaignForm item : campaignForms) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("productId", item.getProductId());
        param.addValue("picture", item.getPictureName());
        param.addValue("story", item.getStory());
        batchParams.add(param);
      }
      template.batchUpdate(INSERT_CAMPAIGN_PRODUCT, batchParams.toArray(new MapSqlParameterSource[0]));
      return true;
    } catch (Exception e) {
      log.warn("insertCampaignProduct exception: ", e);
      throw new DatabaseOperationException("Failed to insert campaign product");
    }
  }

  @Override
  public List<CampaignDto> getAllCampaignInfo() {
    String QUERY_ALL_CAMPAIGN = "SELECT id, product_id, picture, story FROM `Campaign`";
    try {
      List<CampaignDto> result = template.query(QUERY_ALL_CAMPAIGN, new MapSqlParameterSource(),new CampaignRowMapper());
      return result;
    } catch (Exception e) {
      log.warn("getAllCampaignInfo exception: ", e);
      return Collections.emptyList();
    }
  }
}
