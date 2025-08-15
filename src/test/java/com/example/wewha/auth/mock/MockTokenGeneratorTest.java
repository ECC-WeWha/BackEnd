package com.example.wewha.auth.mock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MockTokenGeneratorTest {
    @Autowired
    private MockTokenGenerator mockTokenGenerator;

    @Test
    void printToken() {
        String token = mockTokenGenerator.generateTestToken();
        System.out.println("토큰: "+token);  // 콘솔에 JWT 출력
    }
}