package com.example.wewha.comments.dto.comment;

import com.example.wewha.comments.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.ZoneOffset;

@Getter
@Builder
public class CommentResponse {
    private Long commentId;
    private Long postId;
    private String author;     // 닉네임 문자열
    private String content;
    private String createdAt;  // ISO-8601 UTC (…Z)

    public static CommentResponse of(Comment c) {
        // LocalDateTime → UTC ISO-8601(…Z) 문자열
        String createdIso = c.getCreatedAt() == null
                ? null
                : c.getCreatedAt().atOffset(ZoneOffset.UTC).toInstant().toString();

        return CommentResponse.builder()
                .commentId(c.getId())
                .postId(c.getPost().getPostId())
                .author(c.getUser().getNickname() == null ? "익명" : c.getUser().getNickname())
                .content(c.getContent())
                .createdAt(createdIso)
                .build();
    }
}
