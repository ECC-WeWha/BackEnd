package com.example.wewha.friend.controller;

import com.example.wewha.common.dto.ApiResponse;
import com.example.wewha.common.entity.User;
import com.example.wewha.common.exception.CustomException;
import com.example.wewha.common.exception.ErrorCode;
import com.example.wewha.common.repository.UserRepository;
import com.example.wewha.friend.dto.*;
import com.example.wewha.friend.entity.UserFriendship;
import com.example.wewha.friend.service.FriendService;
import com.example.wewha.friend.service.FriendsQueryService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friend-requests")
public class FriendController {

    private final UserRepository userRepository;
    private final FriendService friendService;
    private final FriendsQueryService service;
    private final FriendsQueryService friendsQueryService;

    private Long currentUserId() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a == null) throw new RuntimeException("Unauthenticated");
        Object p = a.getPrincipal();

        // 1) 커스텀 principal에 userId(Long/String) 담겨있는 케이스
        if (p instanceof Long l) return l;
        if (p instanceof String s) {
            // 숫자면 userId, 아니면 email로 간주
            try {
                return Long.valueOf(s);
            } catch (NumberFormatException ignore) {
                User u = userRepository.findByEmail(s)
                        .orElseThrow(() -> new CustomException(ErrorCode.ERR_NOT_FOUND));
                return u.getUserId();
            }
        }
        if (p instanceof UserDetails ud) {
            String username = ud.getUsername(); // 현재 이메일일 가능성 높음
            try {
                return Long.valueOf(username);
            } catch (NumberFormatException ignore) {
                User u = userRepository.findByEmail(username)
                        .orElseThrow(() -> new CustomException(ErrorCode.ERR_NOT_FOUND));
                return u.getUserId();
            }
        }
        throw new RuntimeException("Cannot resolve userId");
    }

    private User currentUser() {
        Long id = currentUserId(); // 이미 email → userId까지 안전 처리됨
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ERR_NOT_FOUND));
    }

    // 친구 신청
    @PostMapping
    public ResponseEntity<ApiResponse<FriendshipDto>> sendFriendRequestRequest(@AuthenticationPrincipal UserDetails userDetails, @RequestBody FriendRequestDto dto) {

        String email = userDetails.getUsername();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.ERR_NOT_FOUND));

        UserFriendship friendship = friendService.sendFriendRequest(currentUser, dto.getReceiverId());
        FriendshipDto friendshipDto = new FriendshipDto(friendship);
        ApiResponse<FriendshipDto> response = ApiResponse.success(
                201,
                "친구 신청이 성공적으로 전송되었습니다.",
                friendshipDto
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 받은 친구 신청 조회
    @GetMapping("/received")
    public ResponseEntity<ApiResponse<List<ReceivedFriendRequestDto>>> showRequests(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.ERR_NOT_FOUND));

        List<ReceivedFriendRequestDto> receivedList = friendService.showRequests(currentUser);
        ApiResponse<List<ReceivedFriendRequestDto>> response = ApiResponse.success(
                200,
                "받은 친구 신청 목록이 성공적으로 조회되었습니다.",
                receivedList
        );

        return ResponseEntity.ok(response);
    }

    // 친구 수락
    @PostMapping("/{requestId}/accept")
    public ResponseEntity<ApiResponse<FriendshipDto>> acceptRequest(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long requestId) {
        String email = userDetails.getUsername();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.ERR_NOT_FOUND));

        UserFriendship friendship = friendService.acceptRequest(currentUser, requestId);
        FriendshipDto friendshipDto = new FriendshipDto(friendship);
        ApiResponse<FriendshipDto> response = ApiResponse.success(
                200,
                "친구 신청을 수락하였습니다.",
                friendshipDto
        );

        return ResponseEntity.ok(response);
    }

    // 친구 거절
    @PostMapping("/{requestId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectRequest(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long requestId) {
        String email = userDetails.getUsername();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.ERR_NOT_FOUND));

        friendService.rejectRequest(currentUser, requestId);
        ApiResponse<Void> response = ApiResponse.success(
                200,
                "친구 신청을 거절하였습니다.",
                null
        );

        return ResponseEntity.ok(response);
    }

    /**
     * 내 친구 목록 조회
     */
    @GetMapping
    public ResponseEntity<StdResponse<List<FriendListItemResponse>>> myFriends(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        Long me = currentUserId();

        List<FriendListItemResponse> data =
                (page != null && size != null)
                        ? friendsQueryService.listPaged(me, page, size)
                        : friendsQueryService.list(me);

        // 프론트 요구 스펙: status/message/data
        return ResponseEntity.ok(new StdResponse<>(200, "친구 목록이 성공적으로 조회되었습니다.", data));
    }

    @Data
    @AllArgsConstructor
    static class StdResponse<T> {
        private int status;
        private String message;
        private T data;
    }
}