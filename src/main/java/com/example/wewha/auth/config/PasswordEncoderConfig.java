package com.example.wewha.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    @Bean(name = "authPasswordEncoder") // <- 이름 분리 (다른 곳에도 passwordEncoder 있으면 충돌 방지)
    public PasswordEncoder authPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
