package com.example.wewha.comments.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

// com.example.wewha.comments.config.CommentSecurityConfig
@Configuration
public class CommentSecurityConfig {

    @Bean
    @Order(1) // 더 구체적인 체인을 먼저
    public SecurityFilterChain commentSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/comments/**")   // ← 댓글 API만 매칭
                .csrf(csrf -> csrf.disable())          // 필요에 맞게 (혹은 .ignoringRequestMatchers("/api/comments/**"))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(reg -> reg
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}
