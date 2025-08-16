package com.example.wewha.users.service;

import com.example.wewha.common.exception.NotFoundException;
import com.example.wewha.users.dto.UserProfileDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileQueryService {

    private final JdbcTemplate jdbc;

    public UserProfileDetailResponse findByUserId(Long targetUserId) {
        String sql = """
            SELECT
                u.user_id,
                u.nickname,
                u.name,
                u.school_year,
                u.birth_year,
                u.academic_status_id,
                u.region_id,
                r.name AS region_name,
                u.profile_image,
                up.major,
                up.language_id,
                l1.name AS language_name,
                up.study_language_id,
                l2.name AS study_language_name,
                up.kakao_id,
                up.insta_id,
                up.introduction,
                u.created_at,
                u.updated_at
            FROM users u
            LEFT JOIN user_profiles up ON up.user_id = u.user_id
            LEFT JOIN languages l1 ON l1.id = up.language_id
            LEFT JOIN languages l2 ON l2.id = up.study_language_id
            LEFT JOIN regions r ON r.id = u.region_id
            WHERE u.user_id = ?
        """;

        try {
            return jdbc.queryForObject(sql, (rs, i) -> new UserProfileDetailResponse(
                    rs.getLong("user_id"),
                    rs.getString("nickname"),
                    rs.getString("name"),
                    (Integer) rs.getObject("school_year"),
                    (Integer) rs.getObject("birth_year"),
                    (Long) rs.getObject("academic_status_id"),
                    (Long) rs.getObject("region_id"),
                    rs.getString("region_name"),
                    rs.getString("profile_image"),
                    rs.getString("major"),
                    (Long) rs.getObject("language_id"),
                    rs.getString("language_name"),
                    (Long) rs.getObject("study_language_id"),
                    rs.getString("study_language_name"),
                    rs.getString("kakao_id"),
                    rs.getString("insta_id"),
                    rs.getString("introduction"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null
            ), targetUserId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("사용자를 찾을 수 없습니다: userId=" + targetUserId);
        }
    }
}
