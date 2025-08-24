package com.example.wewha.friend.service;

import com.example.wewha.common.exception.CustomException;
import com.example.wewha.common.exception.ErrorCode;
import com.example.wewha.common.repository.LanguageRepository;
import com.example.wewha.common.repository.UserProfileRepository;
import com.example.wewha.common.entity.Language;
import com.example.wewha.common.entity.User;
import com.example.wewha.common.entity.UserProfile;
import com.example.wewha.friend.FriendshipStatus;
import com.example.wewha.friend.dto.MatchProfileRequestDto;
import com.example.wewha.friend.dto.MatchProfileResponseDto;
import com.example.wewha.friend.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendMatchingService {
    private final LanguageRepository languageRepository;
    private final UserProfileRepository userProfileRepository;
    private final FriendshipRepository friendshipRepository;

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

    @Transactional(readOnly = true)
    public List<MatchProfileResponseDto> showProfileByStudyLanguage(User currentUser, String languageName) {
        // 1. 파라미터로 받아온 언어와 학습언어가 일치하는 유저 프로필 가져오기
        Language studyLanguage = languageRepository.findByName(languageName)
                .orElseThrow(() -> new CustomException(ErrorCode.ERR_NOT_FOUND));
        List<UserProfile> profiles = userProfileRepository.findByStudyLanguage(studyLanguage);

        // 2. 친구 관계 가져오기
        return profiles.stream()
                .filter(profile -> !profile.getUser().equals(currentUser)) // 자기 자신 제외
                .map(profile -> {
                    var target = profile.getUser();

                    boolean pendingAB = friendshipRepository
                            .existsByRequesterAndReceiverAndStatus(currentUser, target, FriendshipStatus.PENDING);
                    boolean pendingBA = friendshipRepository
                            .existsByRequesterAndReceiverAndStatus(target, currentUser, FriendshipStatus.PENDING);
                    boolean isFriendRequested = friendshipRepository.existsByRequesterAndReceiverAndStatus(
                            currentUser, profile.getUser(), FriendshipStatus.PENDING);
                    boolean acceptedAB = friendshipRepository
                            .existsByRequesterAndReceiverAndStatus(currentUser, target, FriendshipStatus.ACCEPTED);
                    boolean acceptedBA = friendshipRepository
                            .existsByRequesterAndReceiverAndStatus(target, currentUser, FriendshipStatus.ACCEPTED);
                    boolean isFriend = friendshipRepository.existsByRequesterAndReceiverAndStatus(
                            currentUser, profile.getUser(), FriendshipStatus.ACCEPTED);

                    return MatchProfileResponseDto.fromEntity(profile, isFriendRequested, isFriend);
                })
                .toList();
    }
}
