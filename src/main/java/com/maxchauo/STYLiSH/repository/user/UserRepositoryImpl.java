package com.maxchauo.STYLiSH.repository.user;

import com.maxchauo.STYLiSH.dto.product.dto.auth.UserDto;
import com.maxchauo.STYLiSH.dto.product.dto.auth.UserProfileDto;
import com.maxchauo.STYLiSH.dto.product.form.auth.SignUpForm;
import com.maxchauo.STYLiSH.exception.EmailAlreadyExistsException;
import com.maxchauo.STYLiSH.exception.UserClientException;
import com.maxchauo.STYLiSH.exception.UserServerException;
import com.maxchauo.STYLiSH.mapper.user.UserInfoRowMapper;
import com.maxchauo.STYLiSH.mapper.user.UserProfileRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
  private final NamedParameterJdbcTemplate template;

  @Override
  public boolean checkEmailExist(String email) {
    String QUERY_CHECK_EMAIL = "SELECT COUNT(*) FROM `UserInfo` WHERE email = :email";
    MapSqlParameterSource param = new MapSqlParameterSource().addValue("email", email);
    try {
      Integer count = template.queryForObject(QUERY_CHECK_EMAIL, param, Integer.class);
      return count != null && count > 0;
    } catch (Exception e) {
      log.warn("checkEmailExist exception {}", e.getMessage());
      throw new UserServerException("fail to check email existence");
    }
  }

  @Override
  public boolean saveNativeUserInfo(SignUpForm signUpForm) {
    if (checkEmailExist(signUpForm.getEmail())) {
      throw new EmailAlreadyExistsException("email already exists");
    }

    String name = signUpForm.getName();
    String email = signUpForm.getEmail();
    String password = signUpForm.getPassword();
    String provider = signUpForm.getProvider();
    String picture = signUpForm.getPicture();
    String sql = "INSERT INTO `UserInfo` (name, email, password, provider, picture) VALUES (:name, :email, :password, :provider, :picture)";
    MapSqlParameterSource param = new MapSqlParameterSource()
            .addValue("email", email)
            .addValue("name", name)
            .addValue("password", password)
            .addValue("provider", provider)
            .addValue("picture", picture);
    try {
      int count = template.update(sql, param);
      return count > 0;
    } catch (Exception e) {
      log.warn("save exception {}", e.getMessage());
      throw new UserServerException("fail to save user info");
    }
  }

  @Override
  public boolean saveFbUserInfo(SignUpForm signUpForm) {
    if (checkEmailExist(signUpForm.getEmail())) {
      return false;
    }

    String sql = "INSERT INTO `UserInfo` (name, email, password, provider, picture) " +
            "VALUES (:name, :email, NULL, :provider, :picture)";
    MapSqlParameterSource param = new MapSqlParameterSource()
            .addValue("email", signUpForm.getEmail())
            .addValue("name", signUpForm.getName())
            .addValue("provider", signUpForm.getProvider())
            .addValue("picture", signUpForm.getPicture());

    try {
      int count = template.update(sql, param);
      return count > 0;
    } catch (Exception e) {
      log.warn("saveFbUserInfo exception {}", e.getMessage());
      throw new UserServerException("fail to save fb user info");
    }
  }


  @Override
  public UserDto findByEmail(String email) {
    if (email == null || email.trim().isEmpty()) {
      throw new UserClientException("email is null or empty");
    }

    String sql = "SELECT id, name, email, password, provider, picture FROM `UserInfo` WHERE email = :email";
    MapSqlParameterSource param = new MapSqlParameterSource()
            .addValue("email", email);
    try {
      return template.queryForObject(sql, param, new UserInfoRowMapper());
    } catch (EmptyResultDataAccessException e) {
      log.warn("findByEmail exception: {}", e.getMessage());
      return null;
    } catch (Exception e) {
      log.warn("findByEmail unexpected exception: {}", e.getMessage());
      throw new UserServerException("fail to find user by email");
    }
  }

  @Override
  public UserProfileDto findById(long userId){
    String sql = "SELECT id, name, email, provider, picture FROM `UserInfo` WHERE id = :id";
    MapSqlParameterSource param = new MapSqlParameterSource().addValue("id", userId);
    try {
      return template.queryForObject(sql, param, new UserProfileRowMapper());
    } catch (EmptyResultDataAccessException e) {
      log.warn("findById exception: {}", e.getMessage());
      return null;
    } catch (Exception e) {
      log.warn("findById unexpected exception: {}", e.getMessage());
      throw new UserServerException("fail to find user by id");
    }
  }


}
