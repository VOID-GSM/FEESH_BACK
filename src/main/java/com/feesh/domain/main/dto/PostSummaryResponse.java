package com.feesh.domain.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PostSummaryResponse {
    private Long id;
    private String title;

    private String category;
    private Long price;
    private String description;
    private String nickname;
    private String profileImageUrl;
    private Long likeCount;
    private Long commentCount;
    private LocalDateTime createdAt;
}