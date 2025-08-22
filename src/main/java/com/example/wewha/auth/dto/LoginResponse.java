package com.example.wewha.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private Long userId;          // 사용자 ID
    private String nickname;      // 사용자 닉네임
    private String accessToken;   // JWT 또는 인증 토큰
    private String message;       // 로그인 성공 메시지
}
