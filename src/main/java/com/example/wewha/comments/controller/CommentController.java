package com.example.wewha.comments.controller;

import com.example.wewha.comments.dto.comment.CommentResponse;
import com.example.wewha.comments.dto.comment.CreateCommentRequest;
import com.example.wewha.comments.dto.comment.UpdateCommentRequest;
import com.example.wewha.comments.security.SecurityUtils;
import com.example.wewha.comments.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<CommentResponse> create(@Valid @RequestBody CreateCommentRequest req) {
        Long userId = requireUserId(); // 또는 @RequestHeader("X-USER-ID") Long userId (임시)
        return ResponseEntity.ok(commentService.create(userId, req));
    }

    /** PATCH /api/comments/{commentId} */
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> update(
            @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentRequest req
    ) {
        Long userId = requireUserId();
        return ResponseEntity.ok(commentService.update(userId, commentId, req));
    }
}
