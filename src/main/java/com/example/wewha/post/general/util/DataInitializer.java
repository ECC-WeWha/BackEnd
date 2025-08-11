package com.example.wewha.post.general.util;

import com.example.wewha.post.common.domain.Category;
import com.example.wewha.post.common.domain.User;
import com.example.wewha.post.general.repository.CategoryRepository;
import com.example.wewha.post.general.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@Profile("local") // local 프로필일 때만 이 설정이 동작합니다.
public class DataInitializer {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // 1번 User 데이터 생성
            User testUser = User.builder()
                    .email("test@example.com")
                    .nickname("테스트유저")
                    .build();
            userRepository.save(testUser);

            // '일상' 카테고리 데이터 생성
            Category dailyCategory = Category.builder()
                    .name("일상")
                    .build();
            categoryRepository.save(dailyCategory);
        };
    }
}