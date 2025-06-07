package com.maxchauo.STYLiSH.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Log4j2
@Component
public class RateLimiterFilter extends OncePerRequestFilter {

  private final StringRedisTemplate redisTemplate;

  @Value("${rate.limiter.max-requests:10}")
  private int maxRequests;

  @Value("${rate.limiter.window-millis:1000}")
  private long windowMillis;

  public RateLimiterFilter(StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  protected void doFilterInternal(
      @NotNull HttpServletRequest request,
      @NotNull HttpServletResponse response,
      @NotNull FilterChain filterChain)
      throws ServletException, IOException {

    //    String ip = request.getRemoteAddr();
    //String ip = request.getHeader("X-Forwarded-For"); // for testing multiple IPs
    //if (ip == null || ip.isEmpty()) {
    //  ip = request.getRemoteAddr(); // fallback
    //}
    String ip = request.getRemoteAddr();
    String key = "rate_limit:" + ip;
    String now = String.valueOf(System.currentTimeMillis());

    List<String> keys = Collections.singletonList(key); // fit redis api
    List<String> args = List.of(now, String.valueOf(windowMillis), String.valueOf(maxRequests));

    DefaultRedisScript<Long> script = new DefaultRedisScript<>();
    script.setLocation(new ClassPathResource("lua/rate_limiter.lua"));
    script.setResultType(Long.class);

    try {// catch RedisConnectionFailureException to handle Redis
      Long result = redisTemplate.execute(script, keys, args.toArray());
      if (result == 0L) {
        log.warn("IP {} is rate limited", ip);
        response.setStatus(429);
        response.getWriter().write("Too Many Requests - Rate limit exceeded.");
        return;
      }
    } catch (RedisConnectionFailureException e) {
      log.warn("Redis unavailable. Bypassing rate limit. IP={}", ip);
      //let the request pass through while redis is down
    }
    filterChain.doFilter(request, response);
  }
}
