package com.maxchauo.STYLiSH.mapper.product;

import com.maxchauo.STYLiSH.dto.product.dto.product.CampaignDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CampaignRowMapper implements RowMapper<CampaignDto> {

  @Override
  public CampaignDto mapRow(ResultSet rs, int rowNum) throws SQLException {
    CampaignDto campaignDto = new CampaignDto();
    campaignDto.setProductId(rs.getInt("product_id"));
    campaignDto.setPicture(rs.getString("picture"));
    campaignDto.setStory(rs.getString("story"));
    return campaignDto;
  }
}
