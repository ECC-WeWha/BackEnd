package com.example.wewha.post.general.service;

import com.example.wewha.common.entity.User;
import com.example.wewha.common.repository.UserRepository;
import com.example.wewha.post.common.domain.*;
import com.example.wewha.post.general.dto.PostCreateRequest;
import com.example.wewha.post.general.dto.PostCreateResponse;
import com.example.wewha.post.general.repository.*;
import com.example.wewha.post.common.domain.Category;
import com.example.wewha.post.common.domain.Post;
import com.example.wewha.post.common.domain.PostImage;
import com.example.wewha.post.general.repository.CategoryRepository;
import com.example.wewha.post.general.repository.PostImageRepository;
import com.example.wewha.post.general.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final KeywordRepository keywordRepository;
    private final PostKeywordRepository postKeywordRepository;
    private final PostImageRepository postImageRepository;

    @Transactional
    public PostCreateResponse createPost(String userEmail, PostCreateRequest request, List<MultipartFile> images) {

        System.out.println("isAnonymous 값 확인: " + request.isAnonymous());

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Category category = categoryRepository.findByName(request.getCategory())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .isAnonymous(request.isAnonymous()) // <-- 여기! 요청의 isAnonymous 값을 사용합니다.
                .user(user)
                .category(category)
                .build();
        Post savedPost = postRepository.save(post);

        List<String> keywords = request.getKeywords();
        if (keywords != null && !keywords.isEmpty()) {
            keywords.forEach(keywordName -> {
                Keyword keyword = keywordRepository.findByName(keywordName)
                        .orElseGet(() -> keywordRepository.save(new Keyword(keywordName)));
                postKeywordRepository.save(new PostKeyword(savedPost, keyword));
            });
        }

        List<String> imageUrls = Collections.emptyList();

        // images 리스트가 null이거나 비어있지 않은지 확인합니다.
        if (images != null && !images.isEmpty()) {
            // 이미지 파일을 S3 등에 업로드하는 로직을 여기에 구현해야 합니다.
            // 지금은 임시로 URL을 생성하여 테스트합니다.
            imageUrls = images.stream()
                    .map(img -> "https://example.com/images/" + img.getOriginalFilename())
                    .collect(Collectors.toList());

            // 각 이미지 URL을 PostImage 엔티티로 저장합니다.
            imageUrls.forEach(url -> {
                postImageRepository.save(new PostImage(url, savedPost));
            });
        }

        return PostCreateResponse.of(savedPost, imageUrls, keywords);
    }
}
