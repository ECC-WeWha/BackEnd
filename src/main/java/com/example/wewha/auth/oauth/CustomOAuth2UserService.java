package com.example.wewha.auth.oauth;

import com.example.wewha.auth.entity.User;
import com.example.wewha.auth.entity.AcademicStatus;
import com.example.wewha.auth.jwt.JwtTokenProvider;
import com.example.wewha.auth.oauth.OAuth2UserInfo;
import com.example.wewha.auth.oauth.OAuth2UserInfoFactory;
import com.example.wewha.auth.repository.UserRepository;
import com.example.wewha.auth.repository.AcademicStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final AcademicStatusRepository academicStatusRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 1. 제공자 정보
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google, kakao, naver

        // 2. 유저 정보 추출
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());
        String email = userInfo.getEmail();

        if (email == null || email.isEmpty()) {
            throw new OAuth2AuthenticationException("이메일 정보를 받아올 수 없습니다.");
        }

        // 3. 유저 조회 또는 생성
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            AcademicStatus defaultStatus = academicStatusRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("기본 학적 상태가 존재하지 않습니다."));

            return userRepository.save(User.builder()
                    .email(email)
                    .nickname(userInfo.getNickname() != null ? userInfo.getNickname() : "소셜유저")
                    .name(userInfo.getName() != null ? userInfo.getName() : "이름없음")
                    .password(passwordEncoder.encode(UUID.randomUUID().toString())) // 임시 비밀번호
                    .birthYear(2000)
                    .year(1)
                    .academicStatus(defaultStatus)
                    .build()
            );
        });

        // 4. JWT 토큰 발급 (선택: 프론트로 전달하려면 SuccessHandler로 처리)
        String jwtToken = jwtTokenProvider.createToken(user.getEmail());

        // 5. SecurityContext 등록 및 리턴
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2User.getAttributes(),
                "id" // 이 부분은 provider에 따라 "id", "sub", "response.id" 등 맞게 조정 필요
        );
    }
}
