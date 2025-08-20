package com.example.wewha.comments.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCommentRequest {
    @NotNull(message = "postId는 필수입니다.")
    private Long postId;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;
}
