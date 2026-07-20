package com.feesh.domain.comment.dto;

import com.feesh.domain.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {

    private Long commentId;
    private String content;
    private Long authorId;
    private String authorNickname;
    private LocalDateTime createdAt;

    public CommentResponse(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.authorId = comment.getAuthor().getId();
        this.authorNickname = comment.getAuthor().getNickname();
        this.createdAt = comment.getCreatedAt();
    }
}