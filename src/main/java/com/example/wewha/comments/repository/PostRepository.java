package com.example.wewha.comments.repository;

import com.example.wewha.comments.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {}
