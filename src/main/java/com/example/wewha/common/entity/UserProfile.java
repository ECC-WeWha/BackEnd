package com.example.wewha.common.entity;

import com.example.wewha.auth.entity.StudyLanguage;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private Language language;

    @Column(name = "purpose", nullable = false)
    private String purpose;

    @Column(name = "major", nullable = false)
    private String major;

    @Column(name = "korean_topic_score")
    private Integer koreanTopicScore;

    @Column(name = "kakao_id")
    private String kakaoId;

    @Column(name = "insta_id")
    private String instaId;

    @Column(name = "introduction")
    private String introduction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_language_id")
    private Language studyLanguage;
}
