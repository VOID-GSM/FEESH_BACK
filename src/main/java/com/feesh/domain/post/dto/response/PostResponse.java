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
    private String authorNickname;
    private int likeCount;
    private int viewCount;
    private LocalDateTime createdAt;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.authorNickname = post.getAuthor().getNickname();
        this.likeCount = post.getLikeCount();
        this.viewCount = post.getViewCount();
        this.createdAt = post.getCreatedAt();
    }
}