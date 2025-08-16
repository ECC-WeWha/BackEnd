package com.example.wewha.friend.service;

import com.example.wewha.common.exception.NotFoundException;
import com.example.wewha.friend.dto.FriendStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendStatusService {

    private final JdbcTemplate jdbc;

    public FriendStatusResponse getStatus(Long me, Long targetId) {
        if (me.equals(targetId)) {
            return new FriendStatusResponse(false, false, false, "SELF");
        }

        // 대상 유저 존재 확인 (없으면 404)
        Integer cnt = jdbc.queryForObject(
                "SELECT COUNT(*) FROM users WHERE user_id = ?",
                Integer.class, targetId);
        if (cnt == null || cnt == 0) {
            throw new NotFoundException("사용자를 찾을 수 없습니다: userId=" + targetId);
        }

        // 두 사용자 사이의 가장 최신 상태 1건 조회
        var sql = """
            SELECT requester_id, receiver_id, status
              FROM user_friendships
             WHERE (requester_id = ? AND receiver_id = ?)
                OR (requester_id = ? AND receiver_id = ?)
             ORDER BY updated_at DESC
             LIMIT 1
        """;

        return jdbc.query(sql, rs -> {
            if (!rs.next()) {
                // 관계가 전혀 없으면 NONE
                return new FriendStatusResponse(false, false, false, "NONE");
            }
            long requesterId = rs.getLong("requester_id");
            long receiverId  = rs.getLong("receiver_id");
            String status    = rs.getString("status");
            String st = status != null ? status.toUpperCase() : "NONE";

            boolean isFriend       = "ACCEPTED".equals(st);
            boolean requestedByMe  = "PENDING".equals(st) && requesterId == me;
            boolean requestedToMe  = "PENDING".equals(st) && receiverId  == me;

            String label;
            if (isFriend)           label = "FRIENDS";
            else if (requestedByMe) label = "REQUESTED_BY_ME";
            else if (requestedToMe) label = "REQUESTED_TO_ME";
            else                    label = "NONE";

            return new FriendStatusResponse(isFriend, requestedByMe, requestedToMe, label);
        }, me, targetId, targetId, me);
    }
}
