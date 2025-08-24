package com.example.wewha.post.national.controller;

import com.example.wewha.common.dto.ApiResponse;
import com.example.wewha.post.common.dto.*;
import com.example.wewha.post.national.service.NationalPostService;
import com.example.wewha.post.national.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/national-posts")
@RequiredArgsConstructor
public class NationalPostController {
    private final NationalPostService postService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<PostCreateResponse>> createPost(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("postData") PostCreateRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        String userEmail = userDetails.getUsername();

        PostCreateResponse responseData = postService.createPost(userEmail, request, images);
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

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long postId) {
        // TODO: 실제로는 @AuthenticationPrincipal로 받은 사용자와 게시글 작성자가 일치하는지 확인해야 함
        postService.deletePost(postId);
        ApiResponse<Void> response = new ApiResponse<>(204, "게시글이 성공적으로 삭제되었습니다.", null);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostSummaryResponse>>> getNationalPosts(
            // required = false 추가
            @RequestParam(name = "country", required = false) String country,
            Pageable pageable) {

        // getPostsByCountry -> getPosts로 메서드 이름 변경
        Page<PostSummaryResponse> responseData = postService.getPosts(country, pageable);
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