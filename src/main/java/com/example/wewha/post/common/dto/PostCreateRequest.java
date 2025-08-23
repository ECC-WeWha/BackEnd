package com.example.wewha.post.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Setter
public class PostCreateRequest {
    private String category; // ERD의 category_id 대신 category 이름으로 받음
    private String title;
    private String content;
    private List<String> imageUrls;
    private boolean anonymous;
    private List<String> keywords;
}
