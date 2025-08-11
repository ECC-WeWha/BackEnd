package com.example.wewha.post.general.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@SuppressWarnings("unused")
@Getter
@NoArgsConstructor
public class PostCreateRequest {
    private String category; // ERD의 category_id 대신 category 이름으로 받음
    private String title;
    private String content;
    private List<String> imageUrls;
}
