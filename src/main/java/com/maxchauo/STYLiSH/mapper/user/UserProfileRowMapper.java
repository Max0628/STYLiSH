package com.maxchauo.STYLiSH.mapper.user;

import com.maxchauo.STYLiSH.dto.product.dto.auth.UserDto;
import com.maxchauo.STYLiSH.dto.product.dto.auth.UserProfileDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfileRowMapper implements RowMapper<UserProfileDto> {
  @Override
  public UserProfileDto mapRow(ResultSet rs, int rowNum) throws SQLException {
    UserProfileDto userProfileDto = new UserProfileDto();
    userProfileDto.setProvider(rs.getString("provider"));
    userProfileDto.setName(rs.getString("name"));
    userProfileDto.setEmail(rs.getString("email"));
    userProfileDto.setPicture(rs.getString("picture"));
    userProfileDto.setRole(rs.getString("role"));
    return userProfileDto;
  }
}
