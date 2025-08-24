package com.example.wewha.users.service;

import com.example.wewha.auth.entity.AcademicStatus;
import com.example.wewha.auth.jwt.JwtTokenProvider;
import com.example.wewha.auth.repository.AcademicStatusRepository;
import com.example.wewha.auth.repository.RegionRepository;
import com.example.wewha.auth.repository.StudyLanguageRepository; // 주입만 유지(사용 안 해도 무방)
import com.example.wewha.common.entity.Language;
import com.example.wewha.common.entity.Region;
import com.example.wewha.common.entity.User;
import com.example.wewha.common.entity.UserProfile;
import com.example.wewha.common.repository.LanguageRepository;
import com.example.wewha.common.repository.UserProfileRepository;
import com.example.wewha.common.repository.UserRepository;
import com.example.wewha.users.dto.UpdatePasswordRequest;
import com.example.wewha.users.dto.UpdateUserRequest;
import com.example.wewha.users.dto.UserInfoResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final AcademicStatusRepository academicStatusRepository;
    private final RegionRepository regionRepository;
    private final LanguageRepository languageRepository;
    private final StudyLanguageRepository studyLanguageRepository; // 사용하지 않아도 주입 유지
    private final PasswordEncoder passwordEncoder;

    /**
     * 내 정보 조회
     * - 프로필이 없으면 빈 프로필을 자동 생성하여 NPE 방지
     */
    public UserInfoResponse getUserInfo(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String email = jwtTokenProvider.getUsername(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        //
        UserProfile profile = userProfileRepository.findById(user.getUserId())
                .orElse(null);

        return UserInfoResponse.of(user, profile);
    }

    /**
     * 내 정보 수정(PATCH)
     * - null 필드는 건드리지 않음
     * - 프로필이 없으면 생성
     */
    public UserInfoResponse updateUserInfo(UpdateUserRequest updateRequest, HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String email = jwtTokenProvider.getUsername(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        //
        UserProfile profile = userProfileRepository.findById(user.getUserId())
                .orElseGet(() -> {
                    UserProfile p = UserProfile.builder()
                            .user(user)
                            .purpose("")   // 또는 "unknown" 등 팀 기준값
                            .major("")     // 또는 "undecided" 등 팀 기준값
                            .build();
                    return userProfileRepository.save(p);
                });

        // ----- User(계정) 쪽 변경 -----
        if (updateRequest.getNickname() != null) {
            user.setNickname(updateRequest.getNickname());
        }
        if (updateRequest.getName() != null) {
            user.setName(updateRequest.getName());
        }
        if (updateRequest.getYear() != null) {
            user.setYear(updateRequest.getYear());
        }
        if (updateRequest.getBirthYear() != null) {
            user.setBirthYear(updateRequest.getBirthYear());
        }
        if (updateRequest.getAcademicStatusId() != null) {
            AcademicStatus academicStatus = academicStatusRepository.findById(updateRequest.getAcademicStatusId())
                    .orElseThrow(() -> new RuntimeException("유효하지 않은 학적 ID"));
            user.setAcademicStatus(academicStatus);
        }
        if (updateRequest.getRegionId() != null) {
            Region region = regionRepository.findById(updateRequest.getRegionId())
                    .orElseThrow(() -> new RuntimeException("유효하지 않은 지역 ID"));
            user.setRegion(region); // user에 직접 세팅 (profile.getUser() 경유 X)
        }

        // ----- UserProfile(프로필) 쪽 변경 -----
        if (updateRequest.getMajor() != null) {
            profile.setMajor(updateRequest.getMajor());
        }
        if (updateRequest.getIntroduction() != null) {
            profile.setIntroduction(updateRequest.getIntroduction());
        }
        if (updateRequest.getKakaoId() != null) {
            profile.setKakaoId(updateRequest.getKakaoId());
        }
        if (updateRequest.getInstaId() != null) {
            profile.setInstaId(updateRequest.getInstaId());
        }
        if (updateRequest.getLanguageId() != null) {
            Language language = languageRepository.findById(updateRequest.getLanguageId())
                    .orElseThrow(() -> new RuntimeException("유효하지 않은 언어 ID"));
            profile.setLanguage(language);
        }
        if (updateRequest.getStudyLanguageId() != null) {
            Language studyLanguage = languageRepository.findById(updateRequest.getStudyLanguageId())
                    .orElseThrow(() -> new RuntimeException("유효하지 않은 학습 언어 ID"));
            profile.setStudyLanguage(studyLanguage);
        }

        // 저장
        userRepository.save(user);
        userProfileRepository.save(profile);

        return UserInfoResponse.of(user, profile);
    }

    /**
     * 비밀번호 변경
     */
    public void updatePassword(UpdatePasswordRequest passwordRequest, HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String email = jwtTokenProvider.getUsername(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(passwordRequest.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 변경
        user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
        userRepository.save(user);
    }
}
