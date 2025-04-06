package com.maxchauo.STYLiSH.mapper;

import com.maxchauo.STYLiSH.dto.product.dto.VariantDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VariantRowMapper implements RowMapper<VariantDto> {

  @Override
  public VariantDto mapRow(ResultSet rs, int rowNum) throws SQLException {
    VariantDto variantDto = new VariantDto();
    variantDto.setColor_id(rs.getLong("color_id"));
    variantDto.setSize_id(rs.getLong("size_id"));
    variantDto.setProduct_id(rs.getLong("product_id"));
    variantDto.setStock(rs.getLong("stock"));
    return variantDto;
  }
}
