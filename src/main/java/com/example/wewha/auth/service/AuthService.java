package com.example.wewha.auth.service;

import com.example.wewha.auth.dto.LoginRequest;
import com.example.wewha.auth.dto.LoginResponse;
import com.example.wewha.auth.dto.SignupRequest;
import com.example.wewha.auth.dto.SignupResponse;
import com.example.wewha.auth.entity.*;
import com.example.wewha.auth.exception.AuthException;
import com.example.wewha.auth.exception.ErrorCode;
import com.example.wewha.auth.jwt.JwtTokenProvider;
import com.example.wewha.auth.repository.*;
import com.example.wewha.common.entity.Language;
import com.example.wewha.common.entity.Region;
import com.example.wewha.common.entity.User;
import com.example.wewha.common.entity.UserProfile;
import com.example.wewha.common.repository.LanguageRepository;
import com.example.wewha.common.repository.UserProfileRepository;
import com.example.wewha.common.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final AcademicStatusRepository academicStatusRepository;
    private final RegionRepository regionRepository;
    private final LanguageRepository languageRepository;
    private final StudyLanguageRepository studyLanguageRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public SignupResponse signup(SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AuthException(ErrorCode.EMAIL_DUPLICATE);
        }

        AcademicStatus academicStatus = academicStatusRepository.findById(request.getAcademicStatusId())
                .orElseThrow(() -> new AuthException(ErrorCode.INVALID_ACADEMIC_STATUS));

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .name(request.getName())
                .year(request.getYear())
                .birthYear(request.getBirthYear())
                .academicStatus(academicStatus)
                .build();

        User savedUser = userRepository.save(user);

        Region region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new AuthException(ErrorCode.INVALID_REGION));

        Language language = languageRepository.findById(request.getLanguageId())
                .orElseThrow(() -> new AuthException(ErrorCode.INVALID_LANGUAGE));

        Language studyLanguage = languageRepository.findById(request.getStudyLanguageId())
                .orElseThrow(() -> new AuthException(ErrorCode.INVALID_STUDY_LANGUAGE));

        UserProfile profile = UserProfile.builder()
                .user(savedUser)
                .major(request.getMajor())
                .language(language)
                .studyLanguage(studyLanguage)
                .kakaoId(request.getKakaoId())
                .instaId(request.getInstaId())
                .introduction(request.getIntroduction())
                .build();

        userProfileRepository.save(profile);

        return SignupResponse.builder()
                .userId(savedUser.getUserId())
                .email(savedUser.getEmail())
                .nickname(savedUser.getNickname())
                .message("회원가입 성공")
                .build();
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AuthException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException(ErrorCode.INVALID_PASSWORD);
        }

        String token = jwtTokenProvider.createToken(user.getEmail());

        return LoginResponse.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .accessToken(token)
                .message("로그인 성공")
                .build();
    }

    public void logout(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);

        // 실제 서비스에서는 이 토큰을 Redis 등에 저장하여 무효화 처리합니다.
        // 현재는 프론트에서 삭제하도록 안내만 함
        System.out.println("로그아웃 요청됨. 토큰: " + token);
    }

    public void deleteUser(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String email = jwtTokenProvider.getUsername(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException(ErrorCode.USER_NOT_FOUND));

        userProfileRepository.deleteById(user.getUserId()); // 프로필 먼저 삭제
        userRepository.delete(user); // 사용자 삭제
    }

}
