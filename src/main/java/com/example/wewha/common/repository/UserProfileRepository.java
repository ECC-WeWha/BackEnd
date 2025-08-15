// UserProfileRepository.java
package com.example.wewha.common.repository;

import com.example.wewha.common.entity.Language;
import com.example.wewha.common.entity.User;
import com.example.wewha.common.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    boolean existsByUser(User currentUser);

    List<UserProfile> findByStudyLanguage(Language studyLanguage);

    Optional<UserProfile> findByUser(User sender);
}
