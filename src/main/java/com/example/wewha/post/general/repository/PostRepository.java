package com.example.wewha.post.general.repository;

import com.example.wewha.post.common.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // 카테고리 이름으로 게시글 목록을 페이징하여 조회
    Page<Post> findByCategory_Name(String categoryName, Pageable pageable);
}