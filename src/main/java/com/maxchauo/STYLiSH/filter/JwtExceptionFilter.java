package com.maxchauo.STYLiSH.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxchauo.STYLiSH.dto.product.dto.ErrorResponseDto;
import com.maxchauo.STYLiSH.exception.AuthenticationException;
import com.maxchauo.STYLiSH.exception.AuthorizationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  protected void doFilterInternal(
          @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (AuthenticationException e) {
      setErrorResponse(response, HttpStatus.UNAUTHORIZED, e.getMessage());
    } catch (AuthorizationException e) {
      setErrorResponse(response, HttpStatus.FORBIDDEN, e.getMessage());
    } catch (Exception e) {
      setErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "server error");
    }
  }

  private void setErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
    response.setStatus(status.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    ErrorResponseDto errorResponseDto = new ErrorResponseDto(status.value(), message);
    response.getWriter().write(objectMapper.writeValueAsString(errorResponseDto));
  }
}
