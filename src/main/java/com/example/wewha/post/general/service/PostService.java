package com.example.wewha.post.general.service;

import com.example.wewha.post.common.domain.Category;
import com.example.wewha.post.common.domain.Post;
import com.example.wewha.post.common.domain.PostImage;
import com.example.wewha.post.common.domain.User;
import com.example.wewha.post.general.dto.*;
import com.example.wewha.post.general.repository.CategoryRepository;
import com.example.wewha.post.general.repository.PostImageRepository;
import com.example.wewha.post.general.repository.PostRepository;
import com.example.wewha.post.general.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

    @Transactional
    public PostCreateResponse createPost(Long userId, PostCreateRequest request) {
        // 1. 사용자(작성자) 정보 조회
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        // 2. 카테고리 정보 조회 (요청받은 이름으로)
        Category category = categoryRepository.findByName(request.getCategory())
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다."));

        // 3. Post 엔티티 생성 및 저장
        Post newPost = Post.builder()
                .user(author)
                .category(category)
                .title(request.getTitle())
                .content(request.getContent())
                .likeCount(0) // 초기 좋아요 수는 0
                .scrapCount(0) // 초기 스크랩 수는 0
                .build();
        postRepository.save(newPost);

        // 4. 이미지 URL이 있다면 PostImage 엔티티로 변환 후 저장
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            request.getImageUrls().forEach(imageUrl -> {
                PostImage postImage = PostImage.builder()
                        .post(newPost)
                        .imageUrl(imageUrl)
                        .build();
                postImageRepository.save(postImage);
            });
        }

        // 5. 응답 DTO 생성 후 반환
        return PostCreateResponse.of(newPost, request.getImageUrls());
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
