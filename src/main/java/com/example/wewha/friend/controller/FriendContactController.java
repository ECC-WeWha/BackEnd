package com.example.wewha.friend.controller;

import com.example.wewha.friend.dto.FriendContactResponse;
import com.example.wewha.friend.service.FriendContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendContactController {

    private final FriendContactService service;

    private Long currentUserId() {
        var a = SecurityContextHolder.getContext().getAuthentication();
        if (a == null) throw new RuntimeException("Unauthenticated");
        Object p = a.getPrincipal();
        if (p instanceof Long l) return l;
        if (p instanceof String s) return Long.valueOf(s);
        if (p instanceof UserDetails ud) return Long.valueOf(ud.getUsername());
        throw new RuntimeException("Cannot resolve userId");
    }

    /** 친구 연락처 보기 */
    @GetMapping("/{friendId}/contact")
    public ResponseEntity<FriendContactResponse> contact(@PathVariable Long friendId) {
        return ResponseEntity.ok(service.getContact(currentUserId(), friendId));
    }
}