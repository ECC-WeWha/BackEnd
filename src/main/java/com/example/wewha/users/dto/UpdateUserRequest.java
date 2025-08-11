package com.example.wewha.users.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    private String password;
    private String nickname;
    private String name;
    private String email;
    private Long regionId;           // 국적
    private Long academicStatusId;   // 학적
    private Integer year;            // 학년
    private Integer birthYear;       // 년생

    private String major;
    private Long languageId;         // 모국어
    private Long studyLanguageId;    // 학습언어
    private String kakaoId;
    private String instaId;
    private String introduction;
}
