package com.maxchauo.STYLiSH.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
@Log4j2
@Component
public class RateLimiterFilter extends OncePerRequestFilter {

  // store IP addresses and their request timestamps
  private static final ConcurrentHashMap<String, Deque<Long>> ipRequestMap =
      new ConcurrentHashMap<>();

  private static final int MAX_REQUESTS = 10;
  private static final long WINDOW_SIZE_MILLIS = 1000;

  @Override
  protected void doFilterInternal(
          @NotNull HttpServletRequest request,
          @NotNull HttpServletResponse response,
          @NotNull FilterChain filterChain)
          throws ServletException, IOException {

    String ip = request.getHeader("X-Forwarded-For");
    if (ip == null || ip.isBlank()) {
      response.setStatus(400); // Bad Request
      response.getWriter().write("Missing X-Forwarded-For header.");
      return;
    }

    long now = System.currentTimeMillis();
    log.info("RateLimiterFilter processing request from IP: {}", ip);

    //every IP address has its own request history
    Deque<Long> deque = ipRequestMap.computeIfAbsent(ip, k -> new ConcurrentLinkedDeque<>());

    //multithreading safe operation
    synchronized (deque) {
      //remove timeStamp not in sliding window
      while (!deque.isEmpty() && deque.peekFirst() <= now - WINDOW_SIZE_MILLIS) {
        deque.pollFirst();
      }

      if (deque.size() >= MAX_REQUESTS) {
        response.setStatus(429); // Too Many Requests
        response.getWriter().write("Too Many Requests - Rate limit exceeded.");
        log.warn("IP {} is been limited，current request count: {}", ip, deque.size());
        return;
      }
      deque.addLast(now);
    }
    filterChain.doFilter(request, response);
  }
}
