package com.maxchauo.STYLiSH.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

@Log4j2
@Configuration
public class RedisConfig {
  @Value("${spring.data.redis.host}")
  private String redisHost;

  @Value("${spring.data.redis.port}")
  private int redisPort;


  @Value("${spring.data.redis.password}")
  private String redisPwd;

  @Value("${spring.data.redis.timeout}")
  private String redisTimeout;

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
    config.setPassword(redisPwd);
    LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
            .commandTimeout(Duration.parse(redisTimeout))
            .shutdownTimeout(Duration.ofMillis(100))
           .useSsl()
           .disablePeerVerification()
            .build();
    return new LettuceConnectionFactory(config, clientConfig);
  }

  @Bean
  public StringRedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
    return new StringRedisTemplate(connectionFactory);
  }
}
