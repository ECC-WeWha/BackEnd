package com.example.wewha.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "academic_statuses") // 실제 테이블명에 맞춰서 수정
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "academic_status_id")
    private Long id;

    @Column(name = "status_name")
    private String statusName; // 실제 컬럼명에 맞춰 수정
}
