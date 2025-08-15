package com.example.wewha.post.common.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.example.wewha.auth.entity.User;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "Post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Builder.Default // 빌더 사용 시 기본값 설정을 위함
    @Column(name = "like_count", nullable = false)
    private int likeCount = 0;

    @Builder.Default
    @Column(name = "scrap_count", nullable = false)
    private int scrapCount = 0;

    @Builder.Default
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getPostId() {
        return this.id; // 또는 this.getId();
    }

}