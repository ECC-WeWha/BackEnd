package com.example.wewha.post.general.util;

import com.example.wewha.auth.entity.AcademicStatus;
import com.example.wewha.common.entity.Region;
import com.example.wewha.common.entity.User;
import com.example.wewha.auth.repository.AcademicStatusRepository;
import com.example.wewha.auth.repository.RegionRepository;
import com.example.wewha.common.repository.UserRepository;
import com.example.wewha.post.common.domain.Category;
import com.example.wewha.post.common.domain.Post;
import com.example.wewha.post.common.domain.PostImage;
import com.example.wewha.post.general.repository.CategoryRepository;
import com.example.wewha.post.general.repository.PostImageRepository;
import com.example.wewha.post.general.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Profile("local") // local 프로필일 때만 동작합니다.
public class DataInitializer {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

    // 1. User를 만들기 위해 필요한 Repository들을 주입받습니다.
    private final RegionRepository regionRepository;
    private final AcademicStatusRepository academicStatusRepository;
    private final PasswordEncoder passwordEncoder;

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
            Category catFood = categoryRepository.save(Category.builder().name("맛집").build());
            Category catDev = categoryRepository.save(Category.builder().name("개발").build());
            Category catDaily = categoryRepository.save(Category.builder().name("일상").build());


            // --- 3. 게시글 데이터 생성 (이미지 포함) ---
            Post post1 = postRepository.save(Post.builder().user(user1).category(catTravel).title("도쿄 3박 4일 여행 후기").content("정말 즐거운 시간이었습니다!").build());
            postImageRepository.save(PostImage.builder().post(post1).imageUrl("https://example.com/tokyo1.jpg").build());
            postImageRepository.save(PostImage.builder().post(post1).imageUrl("https://example.com/tokyo2.jpg").build());

            Post post2 = postRepository.save(Post.builder().user(user2).category(catFood).title("강남역 인생 맛집 찾았어요").content("여기 파스타가 정말 최고예요!").build());
            postImageRepository.save(PostImage.builder().post(post2).imageUrl("https://example.com/pasta.png").build());

            Post post3 = postRepository.save(Post.builder().user(user3).category(catDev).title("JPA N+1 문제 해결 방법").content("Fetch Join을 사용하면 간단하게 해결할 수 있습니다.").build());
            postImageRepository.save(PostImage.builder().post(post3).imageUrl("https://example.com/jpa_performance.gif").build());


            // --- 4. 게시글 데이터 생성 (이미지 없음) ---
            postRepository.save(Post.builder().user(user1).category(catDaily).title("오늘 날씨 정말 좋네요").content("산책하기 딱 좋은 날씨!").build());
            postRepository.save(Post.builder().user(user2).category(catFood).title("성수동 카페 추천해주세요").content("디저트 맛있는 곳으로 부탁드립니다.").build());
            postRepository.save(Post.builder().user(user3).category(catTravel).title("겨울에 갈만한 국내 여행지").content("따뜻한 남쪽으로 가고 싶어요.").build());
            postRepository.save(Post.builder().user(user1).category(catDev).title("코딩 테스트 준비 시작").content("하루에 한 문제씩 풀어보려고 합니다.").build());
        };
    }
}
