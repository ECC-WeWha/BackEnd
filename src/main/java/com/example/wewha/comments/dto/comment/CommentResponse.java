package com.example.wewha.comments.dto.comment;

import java.time.Instant;

public record CommentResponse(
        Long id,
        Long postId,
        Long authorId,
        String content,
        Instant createdAt,
        Instant updatedAt,
        int likeCount
) {}
