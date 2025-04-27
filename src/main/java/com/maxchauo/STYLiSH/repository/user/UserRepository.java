package com.maxchauo.STYLiSH.repository.user;

import com.maxchauo.STYLiSH.dto.product.dto.auth.UserDto;
import com.maxchauo.STYLiSH.dto.product.dto.auth.UserProfileDto;
import com.maxchauo.STYLiSH.dto.product.form.auth.SignUpForm;


public interface UserRepository {
  boolean checkEmailExist(String email);
  UserDto findByEmail(String email);
  UserProfileDto findById(long userId);
  boolean saveNativeUserInfo(SignUpForm signUpForm);
  boolean saveFbUserInfo(SignUpForm signUpForm);
}
