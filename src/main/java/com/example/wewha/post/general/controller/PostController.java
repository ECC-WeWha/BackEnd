package com.example.wewha.post.general.controller;


import com.example.wewha.common.dto.ApiResponse;
import com.example.wewha.post.general.dto.*;
import com.example.wewha.post.general.dto.PostCreateRequest;
import com.example.wewha.post.general.dto.PostCreateResponse;
import com.example.wewha.post.general.service.PostService;
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
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<PostCreateResponse>> createPost(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("postData") PostCreateRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        // --- 여기를 수정했습니다 ---
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