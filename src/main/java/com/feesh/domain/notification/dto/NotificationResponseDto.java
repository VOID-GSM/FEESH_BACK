package com.feesh.domain.notification.dto;

import com.feesh.domain.comment.entity.Comment;
import com.feesh.domain.notification.entity.Notification;
import com.feesh.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponseDto {

    private Long id;
    private Long senderId;
    private String senderNickname;
    private Long postId;
    private Long commentId;
    private Long parentCommentId;
    private boolean read;
    private LocalDateTime createdAt;

    public static NotificationResponseDto from(Notification notification, User sender, Comment comment) {
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .senderId(notification.getSenderId())
                .senderNickname(sender != null ? sender.getNickname() : null)
                .postId(notification.getPostId())
                .commentId(notification.getCommentId())
                .parentCommentId(comment != null && comment.getParent() != null ? comment.getParent().getId() : null)
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}