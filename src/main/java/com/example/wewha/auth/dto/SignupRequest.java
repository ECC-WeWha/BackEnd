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
    private String loginId;  // 프론트의 "아이디" → loginId로 저장
    private String email;
    private String password;
    private String nickname;
    private String name;
    private Long academicStatusId; // 학적
    private Integer year; // 학년
    private Integer birthYear; // 년생
    private Long regionId; // 국적

}
