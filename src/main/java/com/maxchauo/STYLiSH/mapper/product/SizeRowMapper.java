package com.maxchauo.STYLiSH.mapper.product;

import com.maxchauo.STYLiSH.dto.product.dto.product.SizeDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SizeRowMapper implements RowMapper<SizeDto> {
  @Override
  public SizeDto mapRow(ResultSet rs, int rowNum) throws SQLException {
    SizeDto sizeDto = new SizeDto();
    sizeDto.setId(rs.getLong("id"));
    sizeDto.setSize(rs.getString("size"));
    return sizeDto;
  }
}
