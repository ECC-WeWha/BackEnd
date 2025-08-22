package com.example.wewha.users.dto;

import com.example.wewha.common.entity.User;
import com.example.wewha.common.entity.UserProfile;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponse {
    private Long userId;
    private String email;
    private String nickname;
    private String name;
    private Integer birthYear;
    private Integer year;
    private String status;
    private String nationality;
    private String major;
    private String language;
    private String studyLanguage;
    private String kakaoId;
    private String instaId;
    private String introduction;

    public static UserInfoResponse of(User user, UserProfile profile) {
        return UserInfoResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .name(user.getName())
                .birthYear(user.getBirthYear())
                .year(user.getYear())
                .status(user.getAcademicStatus().getStatusName())
                .nationality(user.getRegion().getName())
                .major(profile.getMajor())
                .language(profile.getLanguage().getName())
                .studyLanguage(profile.getStudyLanguage().getName())
                .kakaoId(profile.getKakaoId())
                .instaId(profile.getInstaId())
                .introduction(profile.getIntroduction())
                .build();
    }
}
