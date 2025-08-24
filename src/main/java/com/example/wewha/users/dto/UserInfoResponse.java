package com.example.wewha.users.dto;

import com.example.wewha.common.entity.User;
import com.example.wewha.common.entity.UserProfile;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponse {
    private Long userId;
    private String loginId;   // 유저가 로그인할 때 쓰는 아이디
    private String email;
    private String nickname;
    private String name;
    private Integer birthYear;
    private Integer year;
    private String status;         // AcademicStatus.statusName
    private String nationality;    // User.region.name
    private String major;
    private String language;       // profile.language.name
    private String studyLanguage;  // profile.studyLanguage.name
    private String kakaoId;
    private String instaId;
    private String introduction;

    public static UserInfoResponse of(User user, UserProfile profile) {
        // user 쪽 null-safe
        String statusName = (user.getAcademicStatus() != null)
                ? user.getAcademicStatus().getStatusName() : null;
        String nationality = (user.getRegion() != null)
                ? user.getRegion().getName() : null;

        // profile 및 연관값 null-safe
        String major = (profile != null) ? profile.getMajor() : null;
        String languageName = (profile != null && profile.getLanguage() != null)
                ? profile.getLanguage().getName() : null;
        String studyLanguageName = (profile != null && profile.getStudyLanguage() != null)
                ? profile.getStudyLanguage().getName() : null;
        String kakaoId = (profile != null) ? profile.getKakaoId() : null;
        String instaId = (profile != null) ? profile.getInstaId() : null;
        String introduction = (profile != null) ? profile.getIntroduction() : null;

        return UserInfoResponse.builder()
                .userId(user.getUserId())
                .loginId(user.getLoginId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .name(user.getName())
                .birthYear(user.getBirthYear())
                .year(user.getYear())
                .status(statusName)
                .nationality(nationality)
                .major(major)
                .language(languageName)
                .studyLanguage(studyLanguageName)
                .kakaoId(kakaoId)
                .instaId(instaId)
                .introduction(introduction)
                .build();
    }
}
