package com.maxchauo.STYLiSH.repository.redis;

import com.fasterxml.jackson.core.type.TypeReference;

import java.time.Duration;

public interface RedisRepository {
  <T> void save(String key, T value, Duration timeoutInSeconds);
  <T> T get(String key, Class<T> clazz);
  <T> T get(String key, TypeReference<T> typeRef);
  void delete(String key);
}