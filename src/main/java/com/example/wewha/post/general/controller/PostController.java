package com.example.wewha.post.general.controller;

import com.example.wewha.post.common.dto.ApiResponse;
import com.example.wewha.post.general.dto.PostCreateRequest;
import com.example.wewha.post.general.dto.PostCreateResponse;
import com.example.wewha.post.general.dto.PostUpdateRequest;
import com.example.wewha.post.general.dto.PostUpdateResponse;
import com.example.wewha.post.general.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long postId) {
        // TODO: 실제로는 @AuthenticationPrincipal로 받은 사용자와 게시글 작성자가 일치하는지 확인해야 함
        postService.deletePost(postId);
        ApiResponse<Void> response = new ApiResponse<>(204, "게시글이 성공적으로 삭제되었습니다.", null);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}