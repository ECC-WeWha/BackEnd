package com.example.wewha.friend.dto;

public record FriendListItemResponse(
        Long friendId,
        String nickname,
        String profileImage,
        String kakaoId,
        String instagramId
) {}