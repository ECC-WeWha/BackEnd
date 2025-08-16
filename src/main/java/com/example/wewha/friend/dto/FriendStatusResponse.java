package com.example.wewha.friend.dto;

public record FriendStatusResponse(
        boolean isFriend,        // 서로 친구(ACCEPTED)
        boolean requestedByMe,   // 내가 상대에게 보낸 요청이 PENDING
        boolean requestedToMe,   // 상대가 나에게 보낸 요청이 PENDING
        String status            // "FRIENDS" | "REQUESTED_BY_ME" | "REQUESTED_TO_ME" | "NONE" | "SELF"
) {}