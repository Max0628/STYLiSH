package com.maxchauo.STYLiSH.mapper.user;

import com.maxchauo.STYLiSH.dto.product.dto.auth.UserDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserInfoRowMapper implements RowMapper<UserDto> {
  @Override
  public UserDto mapRow(ResultSet rs, int rowNum) throws SQLException {
    UserDto userDto = new UserDto();
    userDto.setId(rs.getLong("id"));
    userDto.setName(rs.getString("name"));
    userDto.setEmail(rs.getString("email"));
    userDto.setPassword(rs.getString("password"));
    userDto.setProvider(rs.getString("provider"));
    userDto.setPicture(rs.getString("picture"));
    return userDto;
  }
}
