package com.example.wewha.friend.dto;

public record FriendSummaryResponse(
        Long userId,
        String nickname,
        String profileImage
) {}
