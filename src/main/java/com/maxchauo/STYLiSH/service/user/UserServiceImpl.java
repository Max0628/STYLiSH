package com.maxchauo.STYLiSH.service.user;

import com.maxchauo.STYLiSH.dto.product.dto.*;
import com.maxchauo.STYLiSH.dto.product.dto.auth.*;
import com.maxchauo.STYLiSH.dto.product.form.auth.SignInForm;
import com.maxchauo.STYLiSH.dto.product.form.auth.SignUpForm;
import com.maxchauo.STYLiSH.exception.AuthorizationException;
import com.maxchauo.STYLiSH.exception.EmailAlreadyExistsException;
import com.maxchauo.STYLiSH.exception.UserClientException;
import com.maxchauo.STYLiSH.exception.MissingTokenException;
import com.maxchauo.STYLiSH.repository.user.UserRepository;
import com.maxchauo.STYLiSH.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.maxchauo.STYLiSH.util.CommonUtil.isValidPassword;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository repo;
  private final JwtUtil jwtUtil;
  private final BCryptPasswordEncoder encoder;
  private final RestTemplate restTemplate;

  public UserServiceImpl(
      UserRepository repo,
      JwtUtil jwtUtil,
      BCryptPasswordEncoder encoder,
      RestTemplate restTemplate) {
    this.repo = repo;
    this.jwtUtil = jwtUtil;
    this.encoder = encoder;
    this.restTemplate = restTemplate;
  }

  @Override
  public DataWrapper<AuthResponseDto> signUp(SignUpForm signUpForm) {
    String email = signUpForm.getEmail();
    String password = signUpForm.getPassword();

    if (repo.checkEmailExist(email)) {
      throw new EmailAlreadyExistsException("email already exists");
    }

    if (!isValidPassword(password)) {
      throw new AuthorizationException("password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character");
    }

    if (signUpForm.getProvider() == null || signUpForm.getProvider().isEmpty()) {
      signUpForm.setProvider("native");
    }
    String hashed = encoder.encode(password);
    signUpForm.setPassword(hashed);
    if(signUpForm.getPicture()==null || signUpForm.getPicture().isEmpty()){
      signUpForm.setPicture("https://img.freepik.com/free-vector/blue-circle-with-white-user_78370-4707.jpg?semt=ais_hybrid&w=740");
    }
    if (!repo.saveNativeUserInfo(signUpForm)) {
      throw new UserClientException("user sign up failed");
    }

    UserDto userDto = repo.findByEmail(email);
    if (userDto == null) {
      throw new UserClientException("user not found");
    }

    UserResponseDto userResponseDto =
        new UserResponseDto(
            userDto.getId(),
            userDto.getProvider(),
            userDto.getName(),
            userDto.getEmail(),
            userDto.getPicture() != null && !userDto.getPicture().isEmpty()
                ? userDto.getPicture()
                : "default_picture_url");

    String token = jwtUtil.generateToken(userDto.getId());
    int expiredInSec = (int) (jwtUtil.getExpirationMs() / 1000);
    AuthResponseDto auth = new AuthResponseDto(token, expiredInSec, userResponseDto);
    return new DataWrapper<>(auth);
  }

  public DataWrapper<AuthResponseDto> signIn(SignInForm signInForm) {
    String provider = signInForm.getProvider();
    if ("native".equals(provider)) {
      return nativeSignIn(signInForm);
    } else if ("facebook".equals(provider)) {
      return facebookSignIn(signInForm);
    } else {
      throw new UserClientException("unsupported provider");
    }
  }


  @Override
  public DataWrapper<AuthResponseDto> nativeSignIn(SignInForm signInForm) {
    String email = signInForm.getEmail();
    String password = signInForm.getPassword();
    UserDto userDto = repo.findByEmail(email);
    if (userDto == null) {
      throw new AuthorizationException("email not found");
    }

    boolean passwordMatch = encoder.matches(password, userDto.getPassword());
    if (!passwordMatch) {
      throw new AuthorizationException("wrong password");
    }

    if (userDto.getPicture() == null || userDto.getPicture().isEmpty()) {
      userDto.setPicture("default_picture_url");
    }
    UserResponseDto userResponseDto =
        new UserResponseDto(
            userDto.getId(),
            userDto.getProvider(),
            userDto.getName(),
            userDto.getEmail(),
            userDto.getPicture());
    String token = jwtUtil.generateToken(userDto.getId());
    int expiredInSec = (int) (jwtUtil.getExpirationMs() / 1000);

    AuthResponseDto auth = new AuthResponseDto(token, expiredInSec, userResponseDto);
    return new DataWrapper<>(auth);
  }

  @Override
  public DataWrapper<AuthResponseDto> facebookSignIn(SignInForm signInForm) {
    String accessToken = signInForm.getAccessToken();
    String url =
        "https://graph.facebook.com/me?fields=name,email,picture.type(large)&access_token="
            + accessToken;
    FacebookProfile fbProfile = restTemplate.getForObject(url, FacebookProfile.class);

    if (fbProfile == null || fbProfile.getEmail() == null) {
      throw new UserClientException("Facebook profile is null or email is missing");
    }

    UserDto userDto = repo.findByEmail(fbProfile.getEmail());

    if (userDto == null) {
      SignUpForm newFbUser = new SignUpForm();
      newFbUser.setEmail(fbProfile.getEmail());
      newFbUser.setName(fbProfile.getName());
      newFbUser.setProvider("facebook");
      newFbUser.setPicture(fbProfile.getPicture().getData().getUrl());
      repo.saveFbUserInfo(newFbUser);
      userDto = repo.findByEmail(fbProfile.getEmail());
    }

    if (userDto.getPicture() == null || userDto.getPicture().isEmpty()) {
      userDto.setPicture("default_picture_url");
    }

    String token = jwtUtil.generateToken(userDto.getId());
    int expiredInSec = (int) (jwtUtil.getExpirationMs() / 1000);
    AuthResponseDto auth =
        new AuthResponseDto(
            token,
            expiredInSec,
            new UserResponseDto(
                userDto.getId(),
                userDto.getProvider(),
                userDto.getName(),
                userDto.getEmail(),
                userDto.getPicture()));
    return new DataWrapper<>(auth);
  }

  @Override
  public DataWrapper<UserProfileDto> getUserProfile(String token) {
    if (token == null || token.isBlank()) {
      throw new MissingTokenException("missing token");
    }

    if (!token.startsWith("Bearer ")) {
      throw new MissingTokenException("token wrong format, missing Bearer");
    }

    String rawToken = token.substring(7);

    long userId = jwtUtil.getUserIdFromToken(rawToken);
    UserProfileDto userProfile = repo.findById(userId);
    DataWrapper<UserProfileDto> wrapper = new DataWrapper<>();
    wrapper.setData(userProfile);
    return wrapper;
  }
}
