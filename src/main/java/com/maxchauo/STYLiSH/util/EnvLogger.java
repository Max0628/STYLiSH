package com.maxchauo.STYLiSH.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EnvLogger {

  @Value("${REDIS_HOST:localhost}")
  private String redisHost;

  @Value("${REDIS_PORT:6379}")
  private int redisPort;

  @Value("${REDIS_PASSWORD:}")
  private String redisPassword;

  @Value("${spring.data.redis.ssl.enabled:false}")
  private boolean redisSslEnabled;

  @Value("${spring.data.redis.lettuce.ssl.verify-peer:false}")
  private boolean redisSslVerifyPeer;

  @PostConstruct
  public void logEnv() {
    System.out.println("REDIS_HOST = " + redisHost);
    System.out.println("REDIS_PORT = " + redisPort);
    System.out.println("REDIS_PASSWORD = " + redisPassword);
    System.out.println("spring.data.redis.ssl.enabled = " + redisSslEnabled);
    System.out.println("spring.data.redis.lettuce.ssl.verify-peer = " + redisSslVerifyPeer);
  }
}
