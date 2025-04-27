package com.maxchauo.STYLiSH.filter;

import com.maxchauo.STYLiSH.exception.AuthenticationException;
import com.maxchauo.STYLiSH.exception.AuthorizationException;
import com.maxchauo.STYLiSH.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  public JwtAuthenticationFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");

    if (isPublicEndpoint(request.getRequestURI())) {
      filterChain.doFilter(request, response);
      return;
    }

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new AuthenticationException("missing token");
    }

    String token = authHeader.substring(7);

    if (!jwtUtil.validateToken(token)) {
      throw new AuthorizationException("token is invalid or expired");
    }



    Long userId = jwtUtil.getUserIdFromToken(token);

    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());

    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    filterChain.doFilter(request, response);
  }

  private boolean isPublicEndpoint(String uri) {//can be accessed without authentication
    return uri.startsWith("/product.html") ||
            uri.startsWith("/auth.html") ||
            uri.startsWith("/campaign.html") ||
            uri.startsWith("/js/product.js") ||
            uri.startsWith("/swagger-ui") ||
            uri.startsWith("/v3/api-docs") ||
            uri.startsWith("/api/v1/user/signin") ||
            uri.startsWith("/api/v1/user/signup") ||
            uri.startsWith("/api/v1/products") ||
            uri.startsWith("/api/v1/marketing") ||
            uri.startsWith("/images") ||
            uri.equals("/");
  }
}