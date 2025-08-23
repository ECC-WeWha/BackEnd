package com.example.wewha.post.common.repository;

import com.example.wewha.post.common.domain.Board;
import com.example.wewha.post.common.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // API 요청 시 '이름'으로 카테고리를 찾아야 하므로 이 메서드를 추가합니다.
    Optional<Category> findByBoardAndCategoryName(Board board, String categoryName);
}