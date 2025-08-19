package com.example.wewha.comments.controller;

import com.example.wewha.comments.dto.comment.CommentResponse;
import com.example.wewha.comments.dto.comment.CreateCommentRequest;
import com.example.wewha.comments.security.SecurityUtils;
import com.example.wewha.comments.service.CommentService;
import com.example.wewha.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 댓글 등록 API
 * POST /api/comments
 */
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private Long requireUserId() {
        Long uid = SecurityUtils.currentUserIdOrNull();
        if (uid == null) throw new RuntimeException("인증 정보가 없습니다.");
        return uid;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> create(@Valid @RequestBody CreateCommentRequest req) {
        Long userId = requireUserId(); // TODO: 실제 인증 사용자 ID로 대체
        CommentResponse data = commentService.create(userId, req);

        ApiResponse<CommentResponse> body =
                new ApiResponse<>(201, "댓글이 성공적으로 등록되었습니다.", data);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
}
