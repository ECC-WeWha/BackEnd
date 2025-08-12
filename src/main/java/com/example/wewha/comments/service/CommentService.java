package com.example.wewha.comments.service;

import com.example.wewha.comments.dto.comment.CommentResponse;
import com.example.wewha.comments.dto.comment.UpdateCommentRequest;
import com.example.wewha.comments.dto.comment.CreateCommentRequest;
import com.example.wewha.comments.entity.Comment;
import com.example.wewha.post.common.domain.Post;
import com.example.wewha.post.common.domain.User;
import com.example.wewha.comments.exception.NotFoundException;
import com.example.wewha.comments.exception.ForbiddenException;
import com.example.wewha.comments.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import com.example.wewha.comments.repository.PostRepository;
import com.example.wewha.comments.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepo;
    private final PostRepository postRepo;
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
}
