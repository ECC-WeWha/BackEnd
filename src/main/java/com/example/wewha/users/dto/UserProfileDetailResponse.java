package com.example.wewha.users.dto;

import java.time.LocalDateTime;

public record UserProfileDetailResponse(
        Long userId,
        String nickname,
        String name,
        Integer schoolYear,
        Integer birthYear,
        Long academicStatusId,
        Long regionId,
        String regionName,
        String profileImage,
        String major,
        Long languageId,
        String languageName,
        Long studyLanguageId,
        String studyLanguageName,
        String kakaoId,
        String instaId,
        String introduction,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
