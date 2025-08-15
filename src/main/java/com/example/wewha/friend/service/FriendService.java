package com.example.wewha.friend.service;

import com.example.wewha.common.entity.User;
import com.example.wewha.common.entity.UserProfile;
import com.example.wewha.common.exception.CustomException;
import com.example.wewha.common.exception.ErrorCode;
import com.example.wewha.common.repository.UserProfileRepository;
import com.example.wewha.common.repository.UserRepository;
import com.example.wewha.friend.FriendshipStatus;
import com.example.wewha.friend.dto.ReceivedFriendRequestDto;
import com.example.wewha.friend.entity.UserFriendship;
import com.example.wewha.friend.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final UserProfileRepository userProfileRepository;

    @Transactional
    public UserFriendship sendFriendRequest(User requester, Long receiverId) {
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new CustomException(ErrorCode.ERR_NOT_FOUND));

        // 이미 친구 요청 보냈거나 받은 경우 (PENDING 상태)
        boolean alreadyRequested = friendshipRepository.existsByRequesterAndReceiverAndStatus(requester, receiver, FriendshipStatus.PENDING)
                || friendshipRepository.existsByRequesterAndReceiverAndStatus(receiver, requester, FriendshipStatus.PENDING);

        if (alreadyRequested) {
            throw new CustomException(ErrorCode.ERR_CONFLICT); // 이미 친구 요청 있음
        }

        // 이미 친구인 경우
        boolean alreadyFriends = friendshipRepository.existsByRequesterAndReceiverAndStatus(requester, receiver, FriendshipStatus.ACCEPTED)
                || friendshipRepository.existsByRequesterAndReceiverAndStatus(receiver, requester, FriendshipStatus.ACCEPTED);

        if (alreadyFriends) {
            throw new CustomException(ErrorCode.ERR_CONFLICT); // 이미 친구 상태
        }

        UserFriendship friendship = UserFriendship.builder()
                .requester(requester)
                .receiver(receiver)
                .status(FriendshipStatus.PENDING)
                .build();

        return friendshipRepository.save(friendship);
    }

    @Transactional(readOnly = true)
    public List<ReceivedFriendRequestDto> showRequests(User currentUser) {
        List<UserFriendship> requests = friendshipRepository
                .findByReceiverAndStatus(currentUser, FriendshipStatus.PENDING);

        // 요청이 없는 경우 빈 리스트 반환
        if (requests.isEmpty()) {
            return List.of();
        }

        return requests.stream()
                .map(friendship -> {
                    User sender = friendship.getRequester();
                    UserProfile profile = userProfileRepository.findByUser(sender)
                            .orElseThrow(() -> new CustomException(ErrorCode.ERR_NOT_FOUND));
                    return ReceivedFriendRequestDto.fromEntity(friendship, profile);
                })
                .toList();
    }

    @Transactional
    public UserFriendship acceptRequest(User currentUser, long requestId) {
        UserFriendship friendship = friendshipRepository.findById(requestId)
                .orElseThrow(() -> new CustomException(ErrorCode.ERR_NOT_FOUND));

        // 수신자가 본인인지 검증
        if (!friendship.getReceiver().getUserId().equals(currentUser.getUserId())) {
            throw new CustomException(ErrorCode.ERR_FORBIDDEN);
        }

        // 이미 처리된 요청인지 검증
        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new CustomException(ErrorCode.ERR_CONFLICT);
        }

        friendship.setStatus(FriendshipStatus.ACCEPTED);
        return friendshipRepository.save(friendship);
    }
}
