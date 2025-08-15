package com.example.wewha.friend.service;

import com.example.wewha.common.exception.CustomException;
import com.example.wewha.common.exception.ErrorCode;
import com.example.wewha.common.repository.LanguageRepository;
import com.example.wewha.common.repository.UserProfileRepository;
import com.example.wewha.common.entity.Language;
import com.example.wewha.common.entity.User;
import com.example.wewha.common.entity.UserProfile;
import com.example.wewha.friend.dto.MatchProfileRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendMatchingService {
    private final LanguageRepository languageRepository;
    private final UserProfileRepository userProfileRepository;

    @Transactional
    public UserProfile saveProfile(User currentUser, MatchProfileRequestDto dto) {
        // 1. dto -> 엔티티로 변환
        Language lang = languageRepository.findByName(dto.getLanguageName())
                .orElseThrow(() -> new CustomException(ErrorCode.ERR_NOT_FOUND));
        Language studyLang = languageRepository.findByName(dto.getStudyLanguageName())
                .orElseThrow(() -> new CustomException(ErrorCode.ERR_NOT_FOUND));
        // 중복 체크
        if (userProfileRepository.existsByUser(currentUser)) {
            throw new CustomException(ErrorCode.ERR_CONFLICT);
        }

        // 2. db에 저장
        UserProfile userProfile = dto.toEntity(currentUser, lang, studyLang);
        UserProfile saved = userProfileRepository.save(userProfile);
        return saved;
    }
}
