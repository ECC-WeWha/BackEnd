package com.example.wewha.friend.service;

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
    public List<FriendSummaryResponse> list(Long me) {
        String sql = """
            SELECT
                CASE WHEN f.requester_id = ? THEN f.receiver_id ELSE f.requester_id END AS friend_user_id,
                u.nickname,
                u.profile_image
            FROM user_friendships f
            JOIN users u
              ON u.user_id = CASE WHEN f.requester_id = ? THEN f.receiver_id ELSE f.requester_id END
            WHERE (f.requester_id = ? OR f.receiver_id = ?)
              AND f.status = 'ACCEPTED'
            ORDER BY f.updated_at DESC
        """;

        return jdbc.query(sql, (rs, i) -> new FriendSummaryResponse(
                rs.getLong("friend_user_id"),
                rs.getString("nickname"),
                rs.getString("profile_image")
        ), me, me, me, me);
    }
}
