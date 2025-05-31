package com.maxchauo.STYLiSH.util;

import com.maxchauo.STYLiSH.exception.JwtTokenInvalidException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
  private SecretKey key;

  @Value("${jwt.secret}")
  private String secretKey;

  @Value("${jwt.expiration.ms}")
  private long expirationTimeMs;

  @PostConstruct
  public void init() {
    this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(long userId, String role) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + expirationTimeMs);
    return Jwts.builder()
        .subject(String.valueOf(userId))
        .claim("role", role)//RBAC
        .issuedAt(now)
        .expiration(expiry)
        .signWith(key)
        .compact();
  }

  public long getExpirationMs() {
    return expirationTimeMs;
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public Long getUserIdFromToken(String token) {
    try {
      String userId =
          Jwts.parser()
                  .verifyWith(key)
                  .build()
                  .parseSignedClaims(token)
                  .getPayload()
                  .getSubject();
      return Long.parseLong(userId);
    } catch (Exception e) {
      throw new JwtTokenInvalidException("token invalid or expired");
    }
  }

  public String getRoleFromToken(String token) {
    try {
      return Jwts.parser()
              .verifyWith(key)
              .build()
              .parseSignedClaims(token)
              .getPayload()
              .get("role", String.class);
    } catch (Exception e) {
      throw new JwtTokenInvalidException("token invalid or expired");
    }
  }
}
