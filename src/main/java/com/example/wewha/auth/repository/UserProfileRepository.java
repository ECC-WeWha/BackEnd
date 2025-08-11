// UserProfileRepository.java
package com.example.wewha.auth.repository;

import com.example.wewha.auth.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
