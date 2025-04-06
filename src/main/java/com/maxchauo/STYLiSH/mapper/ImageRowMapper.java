package com.maxchauo.STYLiSH.mapper;

import com.maxchauo.STYLiSH.dto.product.dto.ImageDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ImageRowMapper implements RowMapper<ImageDto> {

  @Override
  public ImageDto mapRow(ResultSet rs, int rowNum) throws SQLException {
    ImageDto imageDto = new ImageDto();
    imageDto.setUrl(rs.getString("url"));
    imageDto.setProductId(rs.getLong("product_id"));
    return imageDto;
  }
}
