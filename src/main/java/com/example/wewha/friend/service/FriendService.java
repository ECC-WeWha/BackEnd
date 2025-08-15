package com.example.wewha.friend.service;

import com.example.wewha.common.entity.User;
import com.example.wewha.common.exception.CustomException;
import com.example.wewha.common.exception.ErrorCode;
import com.example.wewha.common.repository.UserRepository;
import com.example.wewha.friend.FriendshipStatus;
import com.example.wewha.friend.entity.UserFriendship;
import com.example.wewha.friend.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

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
}
