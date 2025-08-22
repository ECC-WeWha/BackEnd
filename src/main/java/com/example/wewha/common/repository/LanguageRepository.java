// LanguageRepository.java
package com.example.wewha.common.repository;

import com.example.wewha.common.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    Optional<Language> findByName(String languageName);
}
