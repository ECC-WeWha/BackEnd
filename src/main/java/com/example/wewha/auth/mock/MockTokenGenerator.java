package com.example.wewha.auth.mock;

import com.example.wewha.auth.jwt.JwtTokenProvider;
import org.springframework.stereotype.Component;

@Component
public class MockTokenGenerator {
    private final JwtTokenProvider jwtTokenProvider;

    public MockTokenGenerator(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 테스트용 사용자 이메일로 JWT 생성
    public String generateTestToken() {
        // 실제 존재하는 사용자 이메일로 넣어야 함
        String testEmail = "test6@example.com";
        return jwtTokenProvider.createToken(testEmail);
    }
}
