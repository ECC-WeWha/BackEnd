package com.example.wewha.users.controller;

import com.example.wewha.users.dto.UpdateUserRequest;
import com.example.wewha.users.dto.UpdatePasswordRequest;
import com.example.wewha.users.dto.UserInfoResponse;
import com.example.wewha.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 사용자 본인의 정보 조회
     */
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getUserInfo(request));
    }

    /**
     * 사용자 본인의 정보 수정
     */
    @PatchMapping("/me")
    public ResponseEntity<UserInfoResponse> updateUserInfo(@RequestBody UpdateUserRequest updateRequest,
                                                           HttpServletRequest request) {
        return ResponseEntity.ok(userService.updateUserInfo(updateRequest, request));
    }

    @PatchMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest passwordRequest,
                                            HttpServletRequest request) {
        userService.updatePassword(passwordRequest, request);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "비밀번호가 성공적으로 변경되었습니다."
        ));
    }
}
