package com.example.wewha.post.general.dto;

import com.example.wewha.post.common.domain.Post;
import com.example.wewha.post.common.domain.PostImage;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostDetailResponse {
    private final Long userId;
    private final Long postId;
    private final String title;
    private final String content;
    private final String author;
    private final String category;
    private final List<String> imageUrls;
    private final int likeCount;
    private final LocalDateTime createdAt;

    public PostDetailResponse(Post post) {
        this.userId = post.getUser().getId();
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = post.getUser().getNickname();
        this.category = post.getCategory().getName();
        this.imageUrls = post.getImages().stream()
                .map(PostImage::getImageUrl)
                .collect(Collectors.toList());
        this.likeCount = post.getLikeCount();
        this.createdAt = post.getCreatedAt();
    }
}