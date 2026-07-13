package com.feesh.domain.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class MyCommentResponse {
    private Long commentId;
    private Long postId;
    private String comment;
    private LocalDateTime createdAt;
}
