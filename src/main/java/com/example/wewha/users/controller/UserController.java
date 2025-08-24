package com.example.wewha.users.controller;

import com.example.wewha.common.entity.User;
import com.example.wewha.common.exception.CustomException;
import com.example.wewha.common.exception.ErrorCode;
import com.example.wewha.common.repository.UserRepository;
import com.example.wewha.friend.dto.FriendProfileDetailResponse;
import com.example.wewha.friend.service.FriendService;
import com.example.wewha.users.dto.UpdateUserRequest;
import com.example.wewha.users.dto.UpdatePasswordRequest;
import com.example.wewha.users.dto.UserInfoResponse;
import com.example.wewha.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final FriendService FriendService;

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
            String username = ud.getUsername();
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
    @GetMapping("/{userId}/profile")
    public ResponseEntity<MsgData<FriendProfileDetailResponse>> getUserProfile(@PathVariable Long userId) {
        User me = currentUser();
        User target = FriendService.getUserOrThrow(userId);

        // 본인 아니면 친구 여부 검사
        if (!me.getUserId().equals(target.getUserId())) {
            if (!FriendService.areFriends(me, target)) {
                // 요구사항에 403 포맷은 없어서, 여기서는 "프로필을 찾을 수 없습니다."로 감춥니다(404).
                return ResponseEntity.status(404)
                        .body(new MsgData<>("해당 사용자의 프로필을 찾을 수 없습니다.", null));
            }
        }

        try {
            FriendProfileDetailResponse data = FriendService.getProfileView(target);
            return ResponseEntity.ok(new MsgData<>("프로필이 성공적으로 조회되었습니다.", data));
        } catch (CustomException e) {
            // 프로필 자체가 없는 경우
            if (e.getErrorCode() == ErrorCode.ERR_NOT_FOUND) {
                return ResponseEntity.status(404)
                        .body(new MsgData<>("해당 사용자의 프로필을 찾을 수 없습니다.", null));
            }
            // 그 외 예외는 전역 핸들러에서 500 처리되도록 던지거나, 여기서도 메시지로 감싸도 됩니다.
            throw e;
        }
    }

    @Data
    @AllArgsConstructor
    static class MsgData<T> {
        private String message;
        private T data;
    }
}
