package com.example.wewha.post.common.dto;

import com.example.wewha.post.common.domain.Post;
import lombok.Getter;

@Getter
public class PostSummaryResponse {
    private final Long userId;
    private final Long postId;
    private final String title;
    private final String author;
    private final int likes;
    private final String thumbnailUrl;

    public PostSummaryResponse(Post post) {
        this.userId = post.getUser().getUserId();
        this.postId = post.getId();
        this.title = post.getTitle();
        if (post.isAnonymous()) {
            this.author = "익명";
        } else {
            this.author = post.getUser().getNickname();
        }
        this.likes = post.getLikeCount();
        // 게시글에 이미지가 있으면 첫 번째 이미지 URL을, 없으면 null을 썸네일로 사용
        this.thumbnailUrl = post.getImages().isEmpty() ? null : post.getImages().get(0).getImageUrl();
    }
}