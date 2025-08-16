package com.example.wewha.comments.service;

import com.example.wewha.comments.dto.comment.CommentResponse;
<<<<<<< HEAD
import com.example.wewha.comments.dto.comment.UpdateCommentRequest;
=======
>>>>>>> origin/BE/comments-post
import com.example.wewha.comments.dto.comment.CreateCommentRequest;
import com.example.wewha.comments.entity.Comment;
import com.example.wewha.common.repository.UserRepository;
import com.example.wewha.post.common.domain.Post;
import com.example.wewha.common.entity.User;
import com.example.wewha.comments.exception.NotFoundException;
<<<<<<< HEAD
import com.example.wewha.comments.exception.ForbiddenException;
=======
>>>>>>> origin/BE/comments-post
import com.example.wewha.comments.repository.CommentRepository;
import com.example.wewha.post.general.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

<<<<<<< HEAD
import java.util.Objects;

=======
>>>>>>> origin/BE/comments-post
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
<<<<<<< HEAD
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
=======
>>>>>>> origin/BE/comments-post
}
