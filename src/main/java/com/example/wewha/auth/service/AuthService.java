package com.example.wewha.auth.service;

import com.example.wewha.auth.dto.LoginRequest;
import com.example.wewha.auth.dto.LoginResponse;
import com.example.wewha.auth.dto.SignupRequest;
import com.example.wewha.auth.dto.SignupResponse;
import com.example.wewha.auth.exception.AuthException;
import com.example.wewha.auth.exception.ErrorCode;
import com.example.wewha.auth.jwt.JwtTokenProvider;
import com.example.wewha.auth.repository.AcademicStatusRepository;
import com.example.wewha.auth.repository.RegionRepository;
import com.example.wewha.common.entity.Region;
import com.example.wewha.common.entity.User;
import com.example.wewha.common.entity.UserProfile;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     * - loginId: 프론트에서 입력하는 로그인 아이디 (문자/숫자)
     * - email  : 이메일(중복 불가)
     */
    public SignupResponse signup(SignupRequest request) {
        // 아이디/이메일 중복 체크
        if (userRepository.existsByLoginId(request.getLoginId())) {
            // ErrorCode에 LOGIN_ID_DUPLICATE가 없으면, 임시로 EMAIL_DUPLICATE 사용
            throw new AuthException(ErrorCode.EMAIL_DUPLICATE);
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException(ErrorCode.EMAIL_DUPLICATE);
        }

        var academicStatus = academicStatusRepository.findById(request.getAcademicStatusId())
                .orElseThrow(() -> new AuthException(ErrorCode.INVALID_ACADEMIC_STATUS));

        Region region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new AuthException(ErrorCode.INVALID_REGION));

        // User 생성 (loginId 저장)
        User user = User.builder()
                .loginId(request.getLoginId()) // 👈 로그인 아이디
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .name(request.getName())
                .year(request.getYear())
                .birthYear(request.getBirthYear())
                .academicStatus(academicStatus)
                .region(region)
                .build();

        User savedUser = userRepository.save(user);

        // 기본 프로필 생성
        userProfileRepository.save(UserProfile.builder()
                .user(savedUser)
                .build());

        return SignupResponse.builder()
                .userId(savedUser.getUserId())
                .email(savedUser.getEmail())
                .nickname(savedUser.getNickname())
                .message("회원가입 성공")
                .build();
    }

    /**
     * 로그인
     * - 기본: loginId 로 조회
     * - (옵션 호환) loginId로 없고 입력이 이메일 형식이면 이메일로도 조회 시도
     */
    public LoginResponse login(LoginRequest request) {
        String loginId = request.getLoginId(); // 프론트 로그인 아이디

        User user = userRepository.findByLoginId(loginId)
                .orElseGet(() -> {
                    // 구계정 호환: 이메일 형식이면 이메일로도 시도
                    if (loginId != null && loginId.contains("@")) {
                        return userRepository.findByEmail(loginId).orElse(null);
                    }
                    return null;
                });

        if (user == null) {
            throw new AuthException(ErrorCode.USER_NOT_FOUND);
        }

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
        // 실제 서비스에서는 블랙리스트/만료 처리 필요
        System.out.println("로그아웃 요청됨. 토큰: " + token);
    }

    public void deleteUser(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String email = jwtTokenProvider.getUsername(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException(ErrorCode.USER_NOT_FOUND));

        // 구현에 따라 프로필 삭제 방식이 다를 수 있음
        userProfileRepository.deleteById(user.getUserId());
        userRepository.delete(user);
    }
}
