package com.example.wewha.auth.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    // User 테이블 필드
    private String email;
    private String password;
    private String nickname;
    private String name;
    private Long academicStatusId; // 학적
    private Integer year; // 학년
    private Integer birthYear; // 년생
    private Long regionId; // 국적 (user_profiles 테이블에 저장됨)

    // UserProfiles 테이블 필드
    private String major;
    private Long languageId; // 모국어
    private Long studyLanguageId; // 학습언어
    private String kakaoId;
    private String instaId;
    private String introduction;
}
