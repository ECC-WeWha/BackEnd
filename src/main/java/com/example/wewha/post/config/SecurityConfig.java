package com.example.wewha.post.config;

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
        http
                // 1. CSRF 보안 기능 비활성화 (Stateless한 REST API에서는 비활성화가 일반적)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. 요청 경로별 접근 권한 설정
                .authorizeHttpRequests(authorize -> authorize
                        // H2 콘솔 경로는 인증 없이 모두 허용
                        .requestMatchers("/h2-console/**").permitAll()
                        // '/api/**'로 시작하는 모든 요청은 일단 모두 허용
                        .requestMatchers("/api/**").permitAll()
                        // 그 외 다른 모든 요청도 일단 허용
                        .anyRequest().permitAll()
                )

                // 3. H2 콘솔을 위한 프레임 옵션 설정 (이전과 동일)
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }
}
