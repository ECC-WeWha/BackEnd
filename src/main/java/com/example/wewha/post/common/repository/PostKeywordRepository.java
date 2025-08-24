package com.example.wewha.post.common.repository;

import com.example.wewha.post.common.domain.PostKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostKeywordRepository extends JpaRepository<PostKeyword, Long> {
}