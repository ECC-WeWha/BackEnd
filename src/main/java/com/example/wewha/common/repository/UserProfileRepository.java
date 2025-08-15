// UserProfileRepository.java
package com.example.wewha.common.repository;

import com.example.wewha.common.entity.User;
import com.example.wewha.common.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    boolean existsByUser(User currentUser);
}
