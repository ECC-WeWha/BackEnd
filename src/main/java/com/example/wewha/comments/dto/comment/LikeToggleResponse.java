package com.example.wewha.comments.dto.comment;

public record LikeToggleResponse(
        boolean liked,  // 현재 토글 결과: true면 좋아요 상태
        int likeCount   // 최신 좋아요 수
) {}