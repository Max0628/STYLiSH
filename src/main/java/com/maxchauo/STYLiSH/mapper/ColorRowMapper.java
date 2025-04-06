package com.maxchauo.STYLiSH.mapper;

import com.maxchauo.STYLiSH.dto.product.dto.ColorDto;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ColorRowMapper implements RowMapper<ColorDto> {
  @Override
  public ColorDto mapRow(ResultSet rs, int rowNum) throws SQLException {
    ColorDto colorDto = new ColorDto();
    colorDto.setId(rs.getLong("id"));
    colorDto.setName(rs.getString("name"));
    colorDto.setCode(rs.getString("code"));
    return colorDto;
  }
}
