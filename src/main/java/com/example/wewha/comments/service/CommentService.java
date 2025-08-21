package com.example.wewha.comments.service;

import com.example.wewha.comments.entity.Comment;
import com.example.wewha.comments.dto.comment.CreateCommentRequest;
import com.example.wewha.comments.dto.comment.CommentResponse;
import com.example.wewha.comments.repository.CommentRepository;
import com.example.wewha.common.entity.User;
import com.example.wewha.common.repository.UserRepository;
import com.example.wewha.post.common.domain.Post;
import com.example.wewha.post.general.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponse create(Long userId, CreateCommentRequest req) {
        // 게시글/사용자 존재 확인
        Post post = postRepository.findById(req.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        String content = req.getContent() == null ? "" : req.getContent().trim();
        if (content.isEmpty()) {
            throw new IllegalArgumentException("요청 형식이 올바르지 않습니다."); // 400으로 매핑 예정
        }

        Comment saved = commentRepository.save(
                Comment.builder()
                        .post(post)
                        .user(user)
                        .content(content)
                        .build()
        );
        return CommentResponse.of(saved);
    }


    @Transactional
    public void delete(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        // 작성자만 삭제 가능 (User 엔티티의 PK는 userId 임에 주의!)
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment); // 하드 삭제
    }
}
