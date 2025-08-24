package com.example.wewha.post.common.dto;

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
        this.userId = post.getUser().getUserId();
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();

        // 요청 사항 반영: 익명 여부에 따라 작성자 필드를 변경합니다.
        // Post 엔티티에 isAnonymous() 필드가 존재한다고 가정합니다.
        if (post.isAnonymous()) {
            this.author = "익명";
        } else {
            this.author = post.getUser().getNickname();
        }

        this.category = post.getCategory().getCategoryName();
        this.imageUrls = post.getImages().stream()
                .map(PostImage::getImageUrl)
                .collect(Collectors.toList());
        this.likeCount = post.getLikeCount();
        this.createdAt = post.getCreatedAt();
    }
}