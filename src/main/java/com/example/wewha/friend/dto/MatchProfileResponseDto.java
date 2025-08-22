package com.example.wewha.friend.dto;

import com.example.wewha.common.entity.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MatchProfileResponseDto {
    private Long userId;
    private String profileImage;
    private String nickname;
    private String region;
    private String languageName;
    private String studyLanguageName;
    private String purpose;
    private String introduction;
    private String major;
    private boolean isFriendRequested;
    private boolean isFriend;

    public static MatchProfileResponseDto fromEntity(UserProfile profile, boolean isFriendRequested, boolean isFriend) {
        return new MatchProfileResponseDto(
                profile.getUser().getUserId(),
                profile.getUser().getProfileImage(),
                profile.getUser().getNickname(),
                profile.getUser().getRegion().getName(),
                profile.getLanguage().getName(),
                profile.getStudyLanguage().getName(),
                profile.getPurpose(),
                profile.getIntroduction(),
                profile.getMajor(),
                isFriendRequested,
                isFriend
        );
    }
}
