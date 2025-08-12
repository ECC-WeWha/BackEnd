package com.example.wewha.comments.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.Instant;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Table(name = "Comment") // 실제 테이블명이 comments면 "comments"로 변경
public class Comment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;   // 프로젝트의 Post 엔티티 import 경로에 맞춰주세요

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author; // 프로젝트의 User 엔티티 import 경로에 맞춰주세요

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "like_count", nullable = false)
    private int likeCount = 0;
}
