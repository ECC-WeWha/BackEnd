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

    public SignupResponse signup(SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AuthException(ErrorCode.EMAIL_DUPLICATE);
        }

        var academicStatus = academicStatusRepository.findById(request.getAcademicStatusId())
                .orElseThrow(() -> new AuthException(ErrorCode.INVALID_ACADEMIC_STATUS));

        Region region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new AuthException(ErrorCode.INVALID_REGION));

        // region은 User에 세팅
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .name(request.getName())
                .year(request.getYear())
                .birthYear(request.getBirthYear())
                .academicStatus(academicStatus)
                .region(region)   // <-- 여기!
                .build();

        User savedUser = userRepository.save(user);

        // UserProfile에는 region을 넣지 않음
        UserProfile profile = UserProfile.builder()
                .user(savedUser)
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
        // 실제 서비스에서는 블랙리스트/만료처리 필요
        System.out.println("로그아웃 요청됨. 토큰: " + token);
    }

    public void deleteUser(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String email = jwtTokenProvider.getUsername(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException(ErrorCode.USER_NOT_FOUND));

        // 구현에 따라 프로필 삭제 방법이 다를 수 있음
        userProfileRepository.deleteById(user.getUserId());
        userRepository.delete(user);
    }
}
