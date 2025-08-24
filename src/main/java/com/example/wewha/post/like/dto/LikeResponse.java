package com.example.wewha.post.like.dto;

import lombok.Getter;

@Getter
public class LikeResponse {
    private final Long userId;
    private final Long postId;
    private final boolean liked;
    private final int likeCount;

    public LikeResponse(Long userId, Long postId, boolean liked, int likeCount) {
        this.userId = userId;
        this.postId = postId;
        this.liked = liked;
        this.likeCount = likeCount;
    }
}