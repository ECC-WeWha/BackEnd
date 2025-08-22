package com.example.wewha.post.general.util;

import com.example.wewha.auth.entity.AcademicStatus;
import com.example.wewha.common.entity.Region;
import com.example.wewha.common.entity.User;
import com.example.wewha.auth.repository.AcademicStatusRepository;
import com.example.wewha.auth.repository.RegionRepository;
import com.example.wewha.common.repository.UserRepository;
import com.example.wewha.post.common.domain.Category;
import com.example.wewha.post.general.repository.CategoryRepository;
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

    // 1. User를 만들기 위해 필요한 Repository들을 주입받습니다.
    private final RegionRepository regionRepository;
    private final AcademicStatusRepository academicStatusRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // 2. User를 만들기 전에, 먼저 Region과 AcademicStatus 데이터를 생성하고 저장합니다.
            // 이 데이터들이 있어야 User의 region_id와 academic_status_id에 값을 넣을 수 있습니다.
            Region regionKorea = regionRepository.save(Region.builder().name("Korea").build());
            Region regionUSA = regionRepository.save(Region.builder().name("USA").build());
            Region regionJapan = regionRepository.save(Region.builder().name("Japan").build());

            AcademicStatus statusStudent = academicStatusRepository.save(AcademicStatus.builder().statusName("재학생").build());
            AcademicStatus statusGraduated = academicStatusRepository.save(AcademicStatus.builder().statusName("졸업").build());

            // 3. User 데이터 생성 시, 위에서 만든 Region과 AcademicStatus 객체를 설정해줍니다.
            User user1 = userRepository.save(User.builder()
                    .email("user1@example.com")
                    .name("김여행")
                    .nickname("여행가")
                    .password(passwordEncoder.encode("password"))
                    .birthYear(1995)
                    .year(20) // User 엔티티의 필드명에 맞게 수정 (year -> schoolYear)
                    .region(regionKorea) // NOT NULL 필드인 region 설정
                    .academicStatus(statusStudent) // NOT NULL 필드인 academicStatus 설정
                    .build());

            User user2 = userRepository.save(User.builder()
                    .email("user2@example.com")
                    .name("이맛집")
                    .nickname("맛집탐방러")
                    .password(passwordEncoder.encode("password"))
                    .birthYear(1998)
                    .year(21)
                    .region(regionUSA) // NOT NULL 필드인 region 설정
                    .academicStatus(statusStudent) // NOT NULL 필드인 academicStatus 설정
                    .build());

            User user3 = userRepository.save(User.builder()
                    .email("user3@example.com")
                    .name("박개발")
                    .nickname("개발자")
                    .password(passwordEncoder.encode("password"))
                    .birthYear(1992)
                    .year(22)
                    .region(regionJapan) // NOT NULL 필드인 region 설정
                    .academicStatus(statusGraduated) // NOT NULL 필드인 academicStatus 설정
                    .build());

            // 4. 카테고리 데이터 생성 (기존과 동일)
            Category catTravel = categoryRepository.save(Category.builder().name("여행").build());
            // ... (나머지 카테고리 생성 코드)

            // ... (나머지 게시글 생성 코드)
        };
    }
}
