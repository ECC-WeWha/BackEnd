package com.example.wewha.comments.repository;

import com.example.wewha.post.common.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {}
