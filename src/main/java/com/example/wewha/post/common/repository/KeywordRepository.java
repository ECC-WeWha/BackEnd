package com.example.wewha.post.common.repository;

import com.example.wewha.post.common.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Optional<Keyword> findByName(String name);
}