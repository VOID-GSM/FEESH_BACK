package com.feesh.domain.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class MyFeedResponse {
    private Long postId;
    private String title;
    private int likeCount;
    private LocalDateTime createdAt;
}
