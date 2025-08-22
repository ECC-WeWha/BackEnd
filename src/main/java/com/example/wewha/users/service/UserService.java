package com.example.wewha.users.service;

import com.example.wewha.auth.entity.*;
import com.example.wewha.auth.jwt.JwtTokenProvider;
import com.example.wewha.auth.repository.*;
import com.example.wewha.common.entity.Language;
import com.example.wewha.common.entity.Region;
import com.example.wewha.common.entity.User;
import com.example.wewha.common.entity.UserProfile;
import com.example.wewha.common.repository.LanguageRepository;
import com.example.wewha.common.repository.UserProfileRepository;
import com.example.wewha.common.repository.UserRepository;
import com.example.wewha.users.dto.UpdateUserRequest;
import com.example.wewha.users.dto.UserInfoResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.wewha.users.dto.UpdatePasswordRequest;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final AcademicStatusRepository academicStatusRepository;
    private final RegionRepository regionRepository;
    private final LanguageRepository languageRepository;
    private final StudyLanguageRepository studyLanguageRepository;
    private final PasswordEncoder passwordEncoder;


    public UserInfoResponse getUserInfo(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String email = jwtTokenProvider.getUsername(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        UserProfile profile = userProfileRepository.findById(user.getUserId())
                .orElseThrow(() -> new RuntimeException("프로필 정보가 없습니다."));

        return UserInfoResponse.of(user, profile);
    }

    public UserInfoResponse updateUserInfo(UpdateUserRequest updateRequest, HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String email = jwtTokenProvider.getUsername(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        UserProfile profile = userProfileRepository.findById(user.getUserId())
                .orElseThrow(() -> new RuntimeException("프로필 정보가 없습니다."));

        // 사용자(User) 정보 수정
        user.setNickname(updateRequest.getNickname());
        user.setName(updateRequest.getName());
        user.setYear(updateRequest.getYear());
        user.setBirthYear(updateRequest.getBirthYear());

        if (updateRequest.getAcademicStatusId() != null) {
            AcademicStatus academicStatus = academicStatusRepository.findById(updateRequest.getAcademicStatusId())
                    .orElseThrow(() -> new RuntimeException("유효하지 않은 학적 ID"));
            user.setAcademicStatus(academicStatus);
        }

        // 프로필(UserProfile) 정보 수정
        profile.setMajor(updateRequest.getMajor());
        profile.setIntroduction(updateRequest.getIntroduction());
        profile.setKakaoId(updateRequest.getKakaoId());
        profile.setInstaId(updateRequest.getInstaId());

        if (updateRequest.getRegionId() != null) {
            Region region = regionRepository.findById(updateRequest.getRegionId())
                    .orElseThrow(() -> new RuntimeException("유효하지 않은 지역 ID"));
            profile.getUser().setRegion(region);
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
