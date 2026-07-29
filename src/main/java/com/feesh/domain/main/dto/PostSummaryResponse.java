package com.feesh.domain.main.dto;

import com.feesh.domain.post.entity.Category;
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

    public PostSummaryResponse(Long id, String title, Category category, Long price,
                               String content, String nickname, String profileImageUrl,
                               int likeCount, Long commentCount, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.category = category.name();
        this.price = price;
        this.description = (content != null && content.length() > 100)
                ? content.substring(0, 100)
                : content;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.likeCount = (long) likeCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
    }
}