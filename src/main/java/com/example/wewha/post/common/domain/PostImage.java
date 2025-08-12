package com.example.wewha.post.common.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "post_image") // ERD의 테이블명과 일치시킵니다.
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 이미지는 항상 Post와 함께 조회될 필요는 없으므로 LAZY 로딩
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 어떤 게시글에 속한 이미지인지 참조

    @Column(name = "image_url", nullable = false)
    private String imageUrl;
}