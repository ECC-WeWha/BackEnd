package com.example.wewha.post.general.dto;

import com.example.wewha.post.common.domain.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PostCreateResponse {
    private Long userId;
    private Long postId;
    private String title;
    private String content;
    private String author; // User 테이블의 nickname
    private String category; // Category 테이블의 name
    private List<String> imageUrls;
    private List<String> keywords;
    private int likeCount;
    private int scrapCount;
    private LocalDateTime createdAt;

    // 빌더 패턴이나 정적 팩토리 메서드를 사용하면 객체 생성이 편리합니다.
    public static PostCreateResponse of(Post post, List<String> imageUrls, List<String> keywords) {
        PostCreateResponse response = new PostCreateResponse();
        response.userId = post.getUser().getUserId();
        response.postId = post.getId();
        response.title = post.getTitle();
        response.content = post.getContent();
        response.author = post.isAnonymous() ? "익명" : post.getUser().getNickname();
        response.category = post.getCategory().getName();
        response.imageUrls = imageUrls;
        response.keywords = keywords;
        response.likeCount = post.getLikeCount();
        response.scrapCount = post.getScrapCount();
        response.createdAt = post.getCreatedAt();
        return response;
    }
}
