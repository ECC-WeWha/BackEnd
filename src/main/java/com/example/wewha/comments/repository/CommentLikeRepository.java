package com.example.wewha.comments.repository;

import com.example.wewha.comments.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    boolean existsByComment_CommentIdAndUser_UserId(Long commentId, Long userId);
    void deleteByComment_CommentIdAndUser_UserId(Long commentId, Long userId);
}