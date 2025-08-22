package com.example.wewha.friend.dto;

import com.example.wewha.common.entity.UserProfile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDto {
    private Long id;
    private String purpose;
    private String introduction;
    private int koreanTopicScore;
    private String major;
    private String kakaoId;
    private String instaId;

    // 연관된 User 정보
    private String username;
    private String email;
    private String regionName; // User -> Region

    // 연관된 Language 정보
    private String languageName;
    private String studyLanguageName;

    public UserProfileDto(UserProfile profile) {
        this.id = profile.getUserId();
        this.purpose = profile.getPurpose();
        this.introduction = profile.getIntroduction();
        this.koreanTopicScore = profile.getKoreanTopicScore();
        this.major = profile.getMajor();
        this.kakaoId = profile.getKakaoId();
        this.instaId = profile.getInstaId();

        // User 정보
        if(profile.getUser() != null) {
            this.username = profile.getUser().getNickname();
            this.email = profile.getUser().getEmail();
            if(profile.getUser().getRegion() != null) {
                this.regionName = profile.getUser().getRegion().getName();
            }
        }

        // Language 정보
        if(profile.getLanguage() != null) {
            this.languageName = profile.getLanguage().getName();
        }
        if(profile.getStudyLanguage() != null) {
            this.studyLanguageName = profile.getStudyLanguage().getName();
        }
    }
}
