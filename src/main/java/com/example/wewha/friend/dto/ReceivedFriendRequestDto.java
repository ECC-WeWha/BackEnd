package com.example.wewha.friend.dto;

import com.example.wewha.common.entity.User;
import com.example.wewha.common.entity.UserProfile;
import com.example.wewha.friend.entity.UserFriendship;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor

public class ReceivedFriendRequestDto {
    private Long senderId;
    private String profileImage;
    private String nickname;
    private String region;
    private String languageName;
    private String studyLanguageName;
    private String purpose;
    private String introduction;
    private String major;
    private LocalDateTime createdAt;

    public static ReceivedFriendRequestDto fromEntity(UserFriendship userFriendship, UserProfile profile) {
        User sender = userFriendship.getRequester();

        return new ReceivedFriendRequestDto(
                sender.getUserId(),
                sender.getProfileImage(),
                sender.getNickname(),
                sender.getRegion().getName(),
                profile.getLanguage().getName(),
                profile.getStudyLanguage().getName(),
                profile.getPurpose(),
                profile.getIntroduction(),
                profile.getMajor(),
                userFriendship.getCreatedAt()
        );
    }

}
