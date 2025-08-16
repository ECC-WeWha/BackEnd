package com.example.wewha.friend.service;

import com.example.wewha.common.exception.ForbiddenException;
import com.example.wewha.friend.dto.FriendContactResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendContactService {

    private final JdbcTemplate jdbc;

    private boolean isFriends(Long me, Long friendId) {
        String sql = """
            SELECT COUNT(*)
              FROM user_friendships f
             WHERE ((f.requester_id = ? AND f.receiver_id = ?)
                 OR (f.requester_id = ? AND f.receiver_id = ?))
               AND f.status = 'ACCEPTED'
        """;
        Integer c = jdbc.queryForObject(sql, Integer.class, me, friendId, friendId, me);
        return c != null && c > 0;
    }

    /** 친구 연락처: user_profiles.kakao_id, insta_id */
    public FriendContactResponse getContact(Long me, Long friendId) {
        if (!isFriends(me, friendId)) {
            throw new ForbiddenException("친구 관계가 아니어서 연락처를 볼 수 없습니다.");
        }
        String sql = """
            SELECT up.kakao_id, up.insta_id
              FROM user_profiles up
             WHERE up.user_id = ?
        """;
        try {
            return jdbc.queryForObject(sql, (rs, i) ->
                    new FriendContactResponse(
                            rs.getString("kakao_id"),
                            rs.getString("insta_id")
                    ), friendId);
        } catch (EmptyResultDataAccessException e) {
            // 프로필이 없다면 비어있는 값으로 응답
            return new FriendContactResponse(null, null);
        }
    }
}