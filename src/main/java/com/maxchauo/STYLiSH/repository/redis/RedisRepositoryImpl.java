package com.maxchauo.STYLiSH.repository.redis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Log4j2
@Repository
public class RedisRepositoryImpl implements RedisRepository {
  private final StringRedisTemplate redisTemplate;
  private final ObjectMapper objectMapper;

  public RedisRepositoryImpl(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
    this.redisTemplate = redisTemplate;
    this.objectMapper = objectMapper;
  }

  @Override
  public <T> void save(String key, T value, long timeoutInSeconds) {
    try {
      String json = objectMapper.writeValueAsString(value);
      redisTemplate.opsForValue().set(key, json, Duration.ofSeconds(timeoutInSeconds));
      log.info("Saved to Redis: key={}, value={}", key, json);
    } catch (Exception e) {
      throw new RuntimeException("Failed to save to Redis", e);
    }
  }

  @Override
  public <T> T get(String key, Class<T> clazz) {
    try {
      String json = redisTemplate.opsForValue().get(key);
      if (json == null) return null;
      log.info("Retrieved from Redis: key={}, value={}", key, json);
      return objectMapper.readValue(json, clazz);
    } catch (Exception e) {
      throw new RuntimeException("Failed to get from Redis", e);
    }
  }

  @Override
  public void delete(String key) {
    redisTemplate.delete(key);
  }

  // overload for List<CampaignDto>
  public <T> T get(String key, TypeReference<T> typeRef) {
    try {
      String json = redisTemplate.opsForValue().get(key);
      if (json == null) return null;
      log.info("Retrieved from Redis: key={}, value={}", key, json);
      return objectMapper.readValue(json, typeRef);
    } catch (Exception e) {
      throw new RuntimeException("Failed to get list from Redis", e);
    }
  }
}
