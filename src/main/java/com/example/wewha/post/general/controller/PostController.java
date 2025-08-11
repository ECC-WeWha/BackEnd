package com.example.wewha.post.general.controller;

import com.example.wewha.post.common.dto.ApiResponse;
import com.example.wewha.post.general.dto.*;
import com.example.wewha.post.general.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostCreateResponse>> createPost(
            // @AuthenticationPrincipal은 실제 기능 구현 전까지 사용되지 않습니다.
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PostCreateRequest request) {

        // TODO: 사용자 인증 기능 완성 후, 실제 로그인된 사용자 ID를 사용하도록 수정해야 합니다.
        // 현재는 테스트를 위해 임시 ID(1번)를 사용합니다.
        Long tempUserId = 1L;

        PostCreateResponse responseData = postService.createPost(tempUserId, request);

        ApiResponse<PostCreateResponse> response = new ApiResponse<>(201, "게시글이 성공적으로 등록되었습니다.", responseData);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostUpdateResponse>> updatePost(
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest request
            // TODO: 실제로는 @AuthenticationPrincipal로 받은 사용자와 게시글 작성자가 일치하는지 확인해야 함
    ) {
        PostUpdateResponse responseData = postService.updatePost(postId, request);
        ApiResponse<PostUpdateResponse> response = new ApiResponse<>(200, "게시글이 성공적으로 수정되었습니다.", responseData);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostSummaryResponse>>> getPosts(
            @RequestParam(required = false) String category,
            Pageable pageable) {

        Page<PostSummaryResponse> responseData = postService.getPosts(category, pageable);
        ApiResponse<Page<PostSummaryResponse>> response = new ApiResponse<>(200, "게시글 목록 조회 성공!", responseData);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDetailResponse>> getPostDetail(@PathVariable Long postId) {
        PostDetailResponse responseData = postService.getPostDetail(postId);
        ApiResponse<PostDetailResponse> response = new ApiResponse<>(200, "게시글 상세 조회 성공!", responseData);
        return ResponseEntity.ok(response);
    }

}