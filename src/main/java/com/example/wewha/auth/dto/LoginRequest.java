package com.example.wewha.auth.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private Long userId;       // 사용자 ID (로그인 아이디)
    private String password;   // 비밀번호
}
