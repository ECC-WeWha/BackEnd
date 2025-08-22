package com.example.wewha.auth.controller;

import com.example.wewha.auth.dto.LoginRequest;
import com.example.wewha.auth.dto.LoginResponse;
import com.example.wewha.auth.dto.SignupRequest;
import com.example.wewha.auth.dto.SignupResponse;
import com.example.wewha.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
        SignupResponse response = authService.signup(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        authService.logout(request);  // 토큰 추출 및 로그아웃 처리
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "로그아웃 완료"
        ));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(HttpServletRequest request) {
        authService.deleteUser(request);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "회원 탈퇴 완료"
        ));
    }

}
