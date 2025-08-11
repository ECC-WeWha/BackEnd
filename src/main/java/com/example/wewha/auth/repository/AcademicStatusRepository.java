// AcademicStatusRepository.java
package com.example.wewha.auth.repository;

import com.example.wewha.auth.entity.AcademicStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademicStatusRepository extends JpaRepository<AcademicStatus, Long> {
}
