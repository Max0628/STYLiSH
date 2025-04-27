package com.maxchauo.STYLiSH.service.user;

import com.maxchauo.STYLiSH.dto.product.dto.auth.AuthResponseDto;
import com.maxchauo.STYLiSH.dto.product.dto.DataWrapper;
import com.maxchauo.STYLiSH.dto.product.dto.auth.UserProfileDto;
import com.maxchauo.STYLiSH.dto.product.form.auth.SignInForm;
import com.maxchauo.STYLiSH.dto.product.form.auth.SignUpForm;

public interface UserService {
  DataWrapper<AuthResponseDto> signUp(SignUpForm signUpForm);
  DataWrapper<AuthResponseDto> signIn(SignInForm signInForm);
  DataWrapper<AuthResponseDto> nativeSignIn(SignInForm signInForm);
  DataWrapper<AuthResponseDto> facebookSignIn(SignInForm signInForm);
  DataWrapper<UserProfileDto> getUserProfile(String token);
}
