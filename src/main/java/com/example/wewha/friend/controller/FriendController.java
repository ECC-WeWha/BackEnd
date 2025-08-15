package com.example.wewha.friend.controller;

import com.example.wewha.common.dto.ApiResponse;
import com.example.wewha.common.entity.User;
import com.example.wewha.common.exception.CustomException;
import com.example.wewha.common.exception.ErrorCode;
import com.example.wewha.common.repository.UserRepository;
import com.example.wewha.friend.dto.FriendRequestDto;
import com.example.wewha.friend.dto.FriendshipDto;
import com.example.wewha.friend.dto.ReceivedFriendRequestDto;
import com.example.wewha.friend.entity.UserFriendship;
import com.example.wewha.friend.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friend-requests")
public class FriendController {

    private final UserRepository userRepository;
    private final FriendService friendService;

    // 친구 신청
    @PostMapping
    public ResponseEntity<ApiResponse<FriendshipDto>> sendFriendRequestRequest(@AuthenticationPrincipal UserDetails userDetails, @RequestBody FriendRequestDto dto) {

        String email = userDetails.getUsername();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(()->new CustomException(ErrorCode.ERR_NOT_FOUND));

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
                .orElseThrow(()->new CustomException(ErrorCode.ERR_NOT_FOUND));

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
                .orElseThrow(()->new CustomException(ErrorCode.ERR_NOT_FOUND));

        UserFriendship friendship = friendService.acceptRequest(currentUser, requestId);
        FriendshipDto friendshipDto = new FriendshipDto(friendship);
        ApiResponse<FriendshipDto> response = ApiResponse.success(
                200,
                "친구 신청을 수락하였습니다.",
                friendshipDto
        );

        return ResponseEntity.ok(response);
    }
}
