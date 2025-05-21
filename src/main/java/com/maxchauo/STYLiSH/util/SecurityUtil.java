package com.maxchauo.STYLiSH.util;

import com.maxchauo.STYLiSH.exception.UserClientException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

  /**
   * get current user id
   * @return user id
   * @throws UserClientException if user is not authenticated
   */
  public Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() != null) {
      return (Long) authentication.getPrincipal();
    }
    throw new UserClientException("user not authenticated");
  }

  /**
   * instance method to check if user is authenticated
   * @return true if user is authenticated, false otherwise
   */
  public boolean isAuthenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() != null;
  }
}