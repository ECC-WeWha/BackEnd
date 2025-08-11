// LanguageRepository.java
package com.example.wewha.auth.repository;

import com.example.wewha.auth.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
}
