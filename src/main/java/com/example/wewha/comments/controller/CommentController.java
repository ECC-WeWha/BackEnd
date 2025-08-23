package com.example.wewha.comments.controller;

import com.example.wewha.comments.dto.comment.CommentResponse;
import com.example.wewha.comments.dto.comment.CreateCommentRequest;
import com.example.wewha.comments.service.CommentService;
import com.example.wewha.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateCommentRequest req
    ) {
        // TODO: 인증 연동 후 userId를 SecurityContext/JWT에서 추출
        Long userId = 1L;

        CommentResponse data = commentService.create(userId, req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "댓글이 성공적으로 등록되었습니다.", data));
    }
}
