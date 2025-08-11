package com.example.wewha.comments.repository;

import com.example.wewha.comments.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
