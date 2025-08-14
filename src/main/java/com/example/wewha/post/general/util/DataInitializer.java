package com.example.wewha.post.general.util;

import com.example.wewha.post.common.domain.Category;
import com.example.wewha.post.general.repository.CategoryRepository;

import com.example.wewha.auth.entity.User;
import com.example.wewha.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Profile("local") // local 프로필일 때만 이 설정이 동작합니다.
public class DataInitializer {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // --- 1. 유저 데이터 생성 (필수 필드 추가) ---
            User user1 = userRepository.save(User.builder().email("user1@example.com").name("김여행").nickname("여행가").password(passwordEncoder.encode("password")).birthYear(1995).year(20).build());
            User user2 = userRepository.save(User.builder().email("user2@example.com").name("이맛집").nickname("맛집탐방러").password(passwordEncoder.encode("password")).birthYear(1998).year(21).build());
            User user3 = userRepository.save(User.builder().email("user3@example.com").name("박개발").nickname("개발자").password(passwordEncoder.encode("password")).birthYear(1992).year(22).build());

            // --- 2. 카테고리 데이터 생성 (기존과 동일) ---
            Category catTravel = categoryRepository.save(Category.builder().name("여행").build());
            // ... (나머지 카테고리 생성 코드)

            // ... (나머지 게시글 생성 코드)
        };
    }
}