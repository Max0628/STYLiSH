package com.maxchauo.STYLiSH.mapper;

import com.maxchauo.STYLiSH.dto.product.dto.ProductDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<ProductDto> {
  @Override
  public ProductDto mapRow(ResultSet rs, int rowNum) throws SQLException {
    ProductDto productDto = new ProductDto();
    productDto.setId(rs.getLong("id"));
    productDto.setCategory(rs.getString("category"));
    productDto.setTitle(rs.getString("title"));
    productDto.setDescription(rs.getString("description"));
    productDto.setPrice(rs.getLong("price"));
    productDto.setTexture(rs.getString("texture"));
    productDto.setWash(rs.getString("wash"));
    productDto.setPlace(rs.getString("place"));
    productDto.setNote(rs.getString("note"));
    productDto.setStory(rs.getString("story"));
    productDto.setMainImage(rs.getString("main_image_url"));
    return productDto;
  }
}
