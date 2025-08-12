package com.example.wewha.post.general.dto;

import com.example.wewha.post.common.domain.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostCreateResponse {
    private Long userId;
    private Long postId;
    private String title;
    private String content;
    private String author; // User 테이블의 nickname
    private String category; // Category 테이블의 name
    private List<String> imageUrls;
    private int likeCount;
    private LocalDateTime createdAt;

    // 빌더 패턴이나 정적 팩토리 메서드를 사용하면 객체 생성이 편리합니다.
    public static PostCreateResponse of(Post post, List<String> imageUrls) {
        PostCreateResponse response = new PostCreateResponse();
        response.userId = post.getUser().getId();
        response.postId = post.getId();
        response.title = post.getTitle();
        response.content = post.getContent();
        response.author = post.getUser().getNickname();
        response.category = post.getCategory().getName();
        response.imageUrls = imageUrls;
        response.likeCount = post.getLikeCount();
        response.createdAt = post.getCreatedAt();
        return response;
    }
}
