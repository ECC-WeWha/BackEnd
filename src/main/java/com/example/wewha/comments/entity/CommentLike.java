package com.example.wewha.comments.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import com.example.wewha.post.common.domain.User;

import java.time.Instant;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Table(
        name = "CommentLike", // 실제 테이블명이 comment_likes면 변경
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "comment_id"})
)
public class CommentLike {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "like_id")
    private Long likeId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}