package com.example.wewha.friend.service;

import com.example.wewha.friend.dto.FriendListItemResponse;
import com.example.wewha.friend.dto.FriendSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendsQueryService {

    private final JdbcTemplate jdbc;

    /**
     * 나의 친구(수락된) 목록
     * - user_friendships: requester_id, receiver_id, status('ACCEPTED')
     * - users: user_id, nickname, profile_image
     */
    public List<FriendListItemResponse> list(Long me) {
        String sql = """
            SELECT
                CASE WHEN f.requester_id = ? THEN f.receiver_id ELSE f.requester_id END AS friend_user_id,
                u.nickname,
                u.profile_image,
                p.kakao_id,
                p.insta_id
            FROM user_friendships f
            JOIN users u
              ON u.user_id = CASE WHEN f.requester_id = ? THEN f.receiver_id ELSE f.requester_id END
            LEFT JOIN user_profiles p
              ON p.user_id = u.user_id
            WHERE (f.requester_id = ? OR f.receiver_id = ?)
              AND f.status = 'ACCEPTED'
            ORDER BY COALESCE(f.updated_at, f.created_at) DESC
        """;

        return jdbc.query(sql, (rs, i) -> new FriendListItemResponse(
                rs.getLong("friend_user_id"),
                rs.getString("nickname"),
                rs.getString("profile_image"),
                rs.getString("kakao_id"),
                rs.getString("insta_id")
        ), me, me, me, me);
    }

    public List<FriendListItemResponse> listPaged(Long me, int page, int size) {
        int offset = page * size;

        String sql = """
            SELECT
                CASE WHEN f.requester_id = ? THEN f.receiver_id ELSE f.requester_id END AS friend_user_id,
                u.nickname,
                u.profile_image,
                p.kakao_id,
                p.insta_id
            FROM user_friendships f
            JOIN users u
              ON u.user_id = CASE WHEN f.requester_id = ? THEN f.receiver_id ELSE f.requester_id END
            LEFT JOIN user_profiles p
              ON p.user_id = u.user_id
            WHERE (f.requester_id = ? OR f.receiver_id = ?)
              AND f.status = 'ACCEPTED'
            ORDER BY COALESCE(f.updated_at, f.created_at) DESC
            LIMIT ? OFFSET ?
        """;

        return jdbc.query(sql, (rs, i) -> new FriendListItemResponse(
                rs.getLong("friend_user_id"),
                rs.getString("nickname"),
                rs.getString("profile_image"),
                rs.getString("kakao_id"),
                rs.getString("insta_id")
        ), me, me, me, me, size, offset);
    }
}