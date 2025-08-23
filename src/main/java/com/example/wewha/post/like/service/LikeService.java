package com.example.wewha.post.like.service;

import com.example.wewha.post.common.domain.Post;
import com.example.wewha.post.common.domain.PostLike;
import com.example.wewha.common.entity.User;
import com.example.wewha.post.common.repository.PostRepository;
import com.example.wewha.common.repository.UserRepository;
import com.example.wewha.post.common.repository.PostLikeRepository;
import com.example.wewha.post.like.dto.LikeResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public LikeResponse toggleLike(Long userId, Long postId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        // 기존에 좋아요를 눌렀는지 확인
        Optional<PostLike> existingLike = postLikeRepository.findByUserAndPost(user, post);

        if (existingLike.isPresent()) {
            // 좋아요를 이미 누른 상태 -> 좋아요 취소
            postLikeRepository.delete(existingLike.get());
            post.decreaseLikeCount(); // Post 엔티티에 좋아요 수 감소 메소드 필요
            return new LikeResponse(userId, postId, false, post.getLikeCount());
        } else {
            // 좋아요를 누르지 않은 상태 -> 좋아요 추가
            PostLike newLike = PostLike.builder().user(user).post(post).build();
            postLikeRepository.save(newLike);
            post.increaseLikeCount(); // Post 엔티티에 좋아요 수 증가 메소드 필요
            return new LikeResponse(userId, postId, true, post.getLikeCount());
        }
    }
}