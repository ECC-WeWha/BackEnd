package com.example.wewha.comments.service;

import com.example.wewha.comments.dto.comment.LikeToggleResponse;
import com.example.wewha.comments.dto.comment.CommentResponse;
import com.example.wewha.comments.dto.comment.UpdateCommentRequest;
import com.example.wewha.comments.dto.comment.CreateCommentRequest;
import com.example.wewha.comments.entity.Comment;
import com.example.wewha.comments.entity.CommentLike;
import com.example.wewha.comments.entity.Post;
import com.example.wewha.comments.entity.User;
import com.example.wewha.comments.exception.NotFoundException;
import com.example.wewha.exception.ForbiddenException;
import com.example.wewha.comments.repository.CommentLikeRepository;
import com.example.wewha.comments.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import com.example.wewha.comments.repository.PostRepository;
import com.example.wewha.comments.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepo;
    private final PostRepository postRepo;
    private final CommentLikeRepository likeRepo;
    private final UserRepository userRepo;

    public CommentResponse create(Long userId, CreateCommentRequest req) {
        Post post = postRepo.findById(req.postId())
                .orElseThrow(() -> new NotFoundException("post"));
        User author = userRepo.getReferenceById(userId);

        Comment c = new Comment();
        c.setPost(post);
        c.setAuthor(author);
        c.setContent(req.content().trim());

        Comment saved = commentRepo.save(c);
        return new CommentResponse(
                saved.getCommentId(),
                saved.getPost().getPostId(),
                saved.getAuthor().getUserId(),
                saved.getContent(),
                saved.getCreatedAt(),
                saved.getUpdatedAt(),
                saved.getLikeCount()
        );
    }
    /** 댓글 수정: 작성자 본인만 가능 */
    public CommentResponse update(Long userId, Long commentId, UpdateCommentRequest req) {
        Comment c = commentRepo.findById(commentId)
                .orElseThrow(() -> new NotFoundException("comment"));

        if (!Objects.equals(c.getAuthor().getUserId(), userId)) {
            throw new ForbiddenException("작성자만 수정할 수 있습니다.");
        }

        c.setContent(req.content().trim());

        // JPA dirty checking으로 업데이트 반영
        return new CommentResponse(
                c.getCommentId(),
                c.getPost().getPostId(),
                c.getAuthor().getUserId(),
                c.getContent(),
                c.getCreatedAt(),
                c.getUpdatedAt(),
                c.getLikeCount()
        );
    }
    public void delete(Long userId, Long commentId, boolean isAdmin) {
        Comment c = commentRepo.findById(commentId)
                .orElseThrow(() -> new NotFoundException("comment"));

        // 작성자이거나 관리자만 삭제 허용
        Long authorId = c.getAuthor().getUserId();
        if (!isAdmin && (authorId == null || !authorId.equals(userId))) {
            throw new ForbiddenException("작성자만 삭제할 수 있습니다.");
        }

        // comment_likes가 FK ON DELETE CASCADE면 이것만으로 정리됨
        commentRepo.delete(c);
    }
    public LikeToggleResponse toggleLike(Long userId, Long commentId) {
        Comment c = commentRepo.findById(commentId)
                .orElseThrow(() -> new NotFoundException("comment"));

        boolean existed = likeRepo.existsByComment_CommentIdAndUser_UserId(commentId, userId);
        if (existed) {
            likeRepo.deleteByComment_CommentIdAndUser_UserId(commentId, userId);
            c.setLikeCount(Math.max(0, c.getLikeCount() - 1));
            return new LikeToggleResponse(false, c.getLikeCount());
        } else {
            // 동시성 상황에서 유니크 제약으로 DataIntegrityViolationException이 날 수 있어 방어
            try {
                CommentLike like = new CommentLike();
                like.setComment(c);
                like.setUser(userRepo.getReferenceById(userId));
                likeRepo.save(like);
                c.setLikeCount(c.getLikeCount() + 1);
                return new LikeToggleResponse(true, c.getLikeCount());
            } catch (DataIntegrityViolationException e) {
                // 거의 동시에 같은 사용자 좋아요가 들어온 경우: 이미 존재한다고 보고 최신 카운트 반환
                return new LikeToggleResponse(true, c.getLikeCount());
            }
        }
    }
}
