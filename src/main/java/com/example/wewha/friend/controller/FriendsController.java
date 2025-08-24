package com.example.wewha.friend.controller;

import com.example.wewha.common.entity.User;
import com.example.wewha.common.exception.CustomException;
import com.example.wewha.common.exception.ErrorCode;
import com.example.wewha.common.repository.UserRepository;
import com.example.wewha.friend.dto.FriendContactResponse;
import com.example.wewha.friend.service.FriendService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friends")
public class FriendsController {

    private final UserRepository userRepository;
    private final FriendService friendService;

    /** 현재 로그인 사용자(User) 조회 (username이 이메일이어도 동작) */
    private User currentUser() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a == null) throw new RuntimeException("Unauthenticated");

        Object p = a.getPrincipal();

        if (p instanceof String s) {
            try {
                Long id = Long.valueOf(s);
                return userRepository.findById(id)
                        .orElseThrow(() -> new CustomException(ErrorCode.ERR_NOT_FOUND));
            } catch (NumberFormatException ignore) {
                return userRepository.findByEmail(s)
                        .orElseThrow(() -> new CustomException(ErrorCode.ERR_NOT_FOUND));
            }
        }

        if (p instanceof UserDetails ud) {
            String username = ud.getUsername(); // 보통 이메일
            try {
                Long id = Long.valueOf(username);
                return userRepository.findById(id)
                        .orElseThrow(() -> new CustomException(ErrorCode.ERR_NOT_FOUND));
            } catch (NumberFormatException ignore) {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new CustomException(ErrorCode.ERR_NOT_FOUND));
            }
        }

        throw new RuntimeException("Cannot resolve current user");
    }

    /**
     * 친구 연락처 보기
     * - 성공: 200 {"status":200,"message":"친구 연락처 정보가 성공적으로 조회되었습니다.","data":{...}}
     * - 실패(친구 아님): 403 {"status":403,"message":"해당 사용자와 친구 관계가 아닙니다."}
     */
    @GetMapping("/{friendId}/contact")
    public ResponseEntity<StdResponse<FriendContactResponse>> friendContact(@PathVariable Long friendId) {
        User me = currentUser();
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new CustomException(ErrorCode.ERR_NOT_FOUND));

        if (!friendService.areFriends(me, friend)) {
            return ResponseEntity.status(403)
                    .body(new StdResponse<>(403, "해당 사용자와 친구 관계가 아닙니다.", null));
        }

        FriendContactResponse data = friendService.getFriendContact(friend);
        return ResponseEntity.ok(new StdResponse<>(200, "친구 연락처 정보가 성공적으로 조회되었습니다.", data));
    }

    @Data
    @AllArgsConstructor
    static class StdResponse<T> {
        private int status;
        private String message;
        private T data;
    }
}
