package com.example.wewha.post.like.controller;

import com.example.wewha.post.common.dto.ApiResponse;
import com.example.wewha.post.like.dto.LikeResponse;
import com.example.wewha.post.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/api/posts/{postId}/likes")
    public ResponseEntity<ApiResponse<LikeResponse>> toggleLike(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails) {

        // TODO: 로그인 기능 구현 후 실제 사용자 ID 사용
        Long tempUserId = 1L;

        LikeResponse responseData = likeService.toggleLike(tempUserId, postId);

        String message = responseData.isLiked() ? "좋아요가 추가되었습니다." : "좋아요가 취소되었습니다.";
        ApiResponse<LikeResponse> response = new ApiResponse<>(200, message, responseData);

        return ResponseEntity.ok(response);
    }
}