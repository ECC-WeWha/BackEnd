package com.example.wewha.friend.dto;

import com.example.wewha.common.entity.User;
import com.example.wewha.common.entity.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendProfileDetailResponse {
    private Long userId;
    private String profileImage;
    private String bannerImage;     // User에 없다면 null 반환
    private String nickname;
    private String country;         // User.region.name 기준
    private String nativeLanguage;  // profile.language.name (소문자 변환)
    private String learningLanguage;// profile.studyLanguage.name (소문자 변환)
    private String bio;             // profile.introduction
    private String major;
    private String purpose;         // profile.purpose (DB 값 그대로 사용)
    private Integer koreanTopic;    // profile.koreanTopicScore (nullable)
    private String academicStatus;  // User.academicStatus.statusName (nullable)

    public static FriendProfileDetailResponse from(User user, UserProfile profile) {
        String nativeLang = profile.getLanguage() != null ? profile.getLanguage().getName() : null;
        String learningLang = profile.getStudyLanguage() != null ? profile.getStudyLanguage().getName() : null;

        return new FriendProfileDetailResponse(
                user.getUserId(),
                user.getProfileImage(),
                // User 엔티티에 배너 필드가 없다면 null; 있으면 getBannerImage()
                safeBanner(user),
                user.getNickname(),
                user.getRegion() != null ? user.getRegion().getName() : null,
                nativeLang != null ? nativeLang.toLowerCase() : null,
                learningLang != null ? learningLang.toLowerCase() : null,
                profile.getIntroduction(),
                profile.getMajor(),
                profile.getPurpose(),
                profile.getKoreanTopicScore(),
                safeAcademic(user)
        );
    }

    private static String safeBanner(User u) {
        try {
            // User에 getBannerImage()가 있다면 사용
            return (String) User.class.getMethod("getBannerImage").invoke(u);
        } catch (Exception ignore) {
            return null;
        }
    }

    private static String safeAcademic(User u) {
        try {
            Object as = User.class.getMethod("getAcademicStatus").invoke(u);
            if (as == null) return null;
            return (String) as.getClass().getMethod("getStatusName").invoke(as);
        } catch (Exception ignore) {
            return null;
        }
    }
}
