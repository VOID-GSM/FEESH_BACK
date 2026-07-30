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
    private String content;
    private String category;
    private Long price;
    private String authorNickname;
    private String profileImageUrl;
    private Long likeCount;
    private Long commentCount;
    private Long viewCount;
    private LocalDateTime createdAt;
    private boolean liked;

    public PostSummaryResponse(Long id, String title, Category category, Long price,
                               String content, String authorNickname, String profileImageUrl,
                               int likeCount, Long commentCount, LocalDateTime createdAt, boolean liked, int viewCount) {
        this.id = id;
        this.title = title;
        this.category = category.name();
        this.price = price;
        this.content = (content != null && content.length() > 100)
                ? content.substring(0, 100)
                : content;
        this.authorNickname = authorNickname;
        this.profileImageUrl = profileImageUrl;
        this.likeCount = (long) likeCount;
        this.viewCount = (long) viewCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.liked = liked;
    }
}