package com.example.wewha.comments.repository;

import com.example.wewha.post.common.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
