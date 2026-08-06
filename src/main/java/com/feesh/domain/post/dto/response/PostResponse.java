package com.feesh.domain.post.dto.response;

import com.feesh.domain.post.entity.Category;
import com.feesh.domain.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private Category category;
    private Long price;
    private Long authorId;
    private String authorNickname;
    private String profileImageUrl;
    private int likeCount;
    private int viewCount;
    private boolean liked;
    private LocalDateTime createdAt;

    public PostResponse(Post post, boolean liked) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.price = post.getPrice();
        this.authorId = post.getAuthor().getId();
        this.authorNickname = post.getAuthor().getNickname();
        this.profileImageUrl = post.getAuthor().getProfileImageUrl();
        this.likeCount = post.getLikeCount();
        this.viewCount = post.getViewCount();
        this.liked = liked;
        this.createdAt = post.getCreatedAt();
    }
}