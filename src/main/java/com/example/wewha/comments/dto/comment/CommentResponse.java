package com.example.wewha.comments.dto.comment;

import java.time.Instant;

public record CommentResponse(
        Long commentId,
        Long postId,
        Long author,
        String content,
        Instant createdAt
) {}
