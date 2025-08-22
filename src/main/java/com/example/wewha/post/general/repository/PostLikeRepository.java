package com.example.wewha.post.general.repository;

import com.example.wewha.post.common.domain.Post;
import com.example.wewha.post.common.domain.PostLike;
import com.example.wewha.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    // 유저와 게시글로 '좋아요' 기록을 찾는 메소드
    Optional<PostLike> findByUserAndPost(User user, Post post);
}