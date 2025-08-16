package com.example.wewha.users.controller;

import com.example.wewha.users.dto.UserProfileDetailResponse;
import com.example.wewha.users.service.ProfileQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final ProfileQueryService service;

    /** 프로필 상세 조회 */
    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserProfileDetailResponse> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(service.findByUserId(userId));
    }
}