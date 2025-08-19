package com.example.wewha.comments.service;

import com.example.wewha.comments.dto.comment.CommentResponse;
import com.example.wewha.comments.dto.comment.CreateCommentRequest;
import com.example.wewha.comments.entity.Comment;
import com.example.wewha.common.repository.UserRepository;
import com.example.wewha.post.common.domain.Post;
import com.example.wewha.common.entity.User;
import com.example.wewha.common.exception.NotFoundException;
import com.example.wewha.comments.repository.CommentRepository;
import com.example.wewha.post.general.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                saved.getCreatedAt()
        );
    }
}
