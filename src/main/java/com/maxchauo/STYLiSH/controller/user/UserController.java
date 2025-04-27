package com.maxchauo.STYLiSH.controller.user;

import com.maxchauo.STYLiSH.dto.product.dto.DataWrapper;
import com.maxchauo.STYLiSH.dto.product.dto.auth.AuthResponseDto;
import com.maxchauo.STYLiSH.dto.product.dto.auth.UserProfileDto;
import com.maxchauo.STYLiSH.dto.product.form.auth.SignInForm;
import com.maxchauo.STYLiSH.dto.product.form.auth.SignUpForm;
import com.maxchauo.STYLiSH.service.user.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
  private final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @PostMapping("/signup")
  public ResponseEntity<DataWrapper<AuthResponseDto>> signUp(@RequestBody SignUpForm signUpForm) {
    DataWrapper<AuthResponseDto> response = service.signUp(signUpForm);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/signin")
  public ResponseEntity<DataWrapper<AuthResponseDto>> signIn(@RequestBody SignInForm signInForm) {
    DataWrapper<AuthResponseDto> response = service.signIn(signInForm);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/profile")
  public ResponseEntity<DataWrapper<UserProfileDto>> getUserProfile(@RequestHeader("Authorization") String token) {
    DataWrapper<UserProfileDto> response = service.getUserProfile(token);
    return ResponseEntity.ok(response);
  }
}
