package com.example.wewha.post.general.service;

import com.example.wewha.common.entity.User;
import com.example.wewha.common.repository.UserRepository;
import com.example.wewha.post.common.domain.*;
import com.example.wewha.post.general.dto.*;
import com.example.wewha.post.general.repository.*;
import com.example.wewha.post.common.domain.Category;
import com.example.wewha.post.common.domain.Post;
import com.example.wewha.post.common.domain.PostImage;
import com.example.wewha.post.general.repository.CategoryRepository;
import com.example.wewha.post.general.repository.PostImageRepository;
import com.example.wewha.post.general.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Transactional
    public PostUpdateResponse updatePost(Long postId, PostUpdateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다. ID: " + postId));

        // TODO: 로그인한 사용자가 게시글의 작성자인지 확인하는 로직 필요
        // 나중에 할게용 ..

        post.update(request.getTitle(), request.getContent());

        // 변경된 post 객체로 응답 DTO를 생성하여 반환
        return new PostUpdateResponse(post);
    }

    @Transactional
    public void deletePost(Long postId) {
        // 1. ID를 사용해 기존 게시글을 데이터베이스에서 찾아옵니다.
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다. ID: " + postId));

        // TODO: 로그인한 사용자가 게시글의 작성자인지 확인하는 로직 필요

        // 2. 찾아온 게시글을 삭제합니다.
        postRepository.delete(post);
    }

    @Transactional(readOnly = true) // 조회 기능이므로 readOnly = true
    public Page<PostSummaryResponse> getPosts(String category, Pageable pageable) {
        Page<Post> posts;
        if (category != null && !category.isEmpty()) {
            // 카테고리 필터가 있는 경우
            posts = postRepository.findByCategory_Name(category, pageable);
        } else {
            // 카테고리 필터가 없는 경우 (전체 조회)
            posts = postRepository.findAll(pageable);
        }
        // Page<Post>를 Page<PostSummaryResponse>로 변환하여 반환
        return posts.map(PostSummaryResponse::new);
    }

    @Transactional(readOnly = true)
    public PostDetailResponse getPostDetail(Long postId) {
        // 1. ID로 게시글을 찾아옵니다. 없으면 예외를 발생시킵니다.
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다. ID: " + postId));

        // 2. 찾아온 Post 엔티티를 PostDetailResponse DTO로 변환하여 반환합니다.
        return new PostDetailResponse(post);
    }
}
