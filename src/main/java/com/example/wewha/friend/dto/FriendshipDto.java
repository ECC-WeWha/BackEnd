package com.example.wewha.friend.dto;

import com.example.wewha.friend.entity.UserFriendship;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FriendshipDto {
    private Long id;
    private Long requesterId;
    private Long receiverId;
    private String status;
    private LocalDateTime requestedAt;

    public FriendshipDto(UserFriendship friendship) {
        this.id = friendship.getId();
        this.requesterId = friendship.getRequester().getUserId();
        this.receiverId = friendship.getReceiver().getUserId();
        this.status = friendship.getStatus().toString();
        this.requestedAt = friendship.getCreatedAt();
    }
}
