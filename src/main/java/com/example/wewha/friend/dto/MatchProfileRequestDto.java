package com.example.wewha.friend.dto;

import com.example.wewha.common.entity.Language;
import com.example.wewha.common.entity.User;
import com.example.wewha.common.entity.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class MatchProfileRequestDto {
    private String languageName;
    private String studyLanguageName;
    private String purpose;
    private String introduction;
    private int koreanTopicScore;
    private String major;
    private String kakaoId;
    private String instaId;

    public UserProfile toEntity(User user, Language language, Language studyLanguage) {
        return UserProfile.builder()
                .user(user)
                .language(language)
                .studyLanguage(studyLanguage)
                .purpose(purpose)
                .introduction(introduction)
                .koreanTopicScore(koreanTopicScore)
                .major(major)
                .kakaoId(kakaoId)
                .instaId(instaId)
                .build();
    }
}
