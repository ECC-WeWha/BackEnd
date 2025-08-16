package com.example.wewha.friend.controller;

import com.example.wewha.friend.dto.FriendStatusResponse;
import com.example.wewha.friend.service.FriendStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendStatusController {

    private final FriendStatusService service;

    private Long currentUserId() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a == null) throw new RuntimeException("Unauthenticated");
        Object p = a.getPrincipal();
        if (p instanceof Long l) return l;
        if (p instanceof String s) return Long.valueOf(s);
        if (p instanceof UserDetails ud) return Long.valueOf(ud.getUsername());
        throw new RuntimeException("Cannot resolve userId");
    }

    /** 친구 상태 확인 (요청 여부 포함) */
    @GetMapping("/{targetId}/status")
    public ResponseEntity<FriendStatusResponse> status(@PathVariable Long targetId) {
        return ResponseEntity.ok(service.getStatus(currentUserId(), targetId));
    }
}

