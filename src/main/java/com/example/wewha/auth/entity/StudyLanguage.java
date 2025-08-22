package com.example.wewha.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "study_languages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyLanguage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_language_id")
    private Long studyLanguageId;

    @Column(name = "name", nullable = false)
    private String name;
}
