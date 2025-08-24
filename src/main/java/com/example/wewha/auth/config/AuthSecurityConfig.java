package com.example.wewha.auth.config;

import com.example.wewha.auth.jwt.JwtAuthenticationFilter;
import com.example.wewha.auth.jwt.JwtTokenProvider;
import com.example.wewha.auth.oauth.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration("authSecurityConfig") // <- 빈 이름 명시 (중복 방지)
@RequiredArgsConstructor
public class AuthSecurityConfig {    // <- 클래스명 변경 (중복 방지)

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // cors 설정
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 1. CSRF, 세션 관리 설정 (Stateless API를 위함)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 2. H2 콘솔을 위한 프레임 옵션 허용
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/h2-console/**").permitAll() // H2 콘솔
                        .requestMatchers("/api/auth/**", "/oauth2/**", "/login/**").permitAll() // 인증 관련 경로
                        .requestMatchers(HttpMethod.GET, "/api/posts", "/api/posts/**").permitAll() // 게시글 조회 API
                        // 생성(POST), 수정(PATCH), 삭제(DELETE)는 인증된 사용자만 가능
                        .requestMatchers(HttpMethod.POST, "/api/posts", "/api/national-posts").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/posts/**", "/api/national-posts/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/posts/**", "/api/national-posts/**").authenticated()
                        //.requestMatchers("/api/friend-matching/**").permitAll()  // 친구 매칭 api 테스트용
                        .anyRequest().authenticated()
                )

                // 4. OAuth2 로그인 설정
                /*
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                )
                */
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "https://wewha.netlify.app"
        ));
        config.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Content-Type","Authorization","X-Requested-With"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean(name = "authAuthenticationManager") // <- 이름 분리
    public AuthenticationManager authAuthenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
