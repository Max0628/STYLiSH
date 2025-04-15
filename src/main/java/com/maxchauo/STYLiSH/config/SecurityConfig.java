package com.maxchauo.STYLiSH.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            authorizeRequests ->
                authorizeRequests.requestMatchers("/product.html", "js/product.js").permitAll() // allow html can be access
                    .requestMatchers("/api/v1/admin/upload").permitAll() // allow form api can go into server.
                    .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll() // allow swagger api
                    .requestMatchers("/api/**").permitAll() // api need to be authenticated
                    .requestMatchers("/images/**").permitAll() // image url can be access
                    .anyRequest().denyAll());
    return http.build();
  }
}
