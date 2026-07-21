package com.feesh.domain.notification.dto;

import com.feesh.domain.notification.entity.Notification;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponseDto {

    private Long id;
    private Long senderId;
    private Long postId;
    private Long commentId;
    private boolean isRead;
    private LocalDateTime createdAt;

    public static NotificationResponseDto from(Notification notification) {
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .senderId(notification.getSenderId())
                .postId(notification.getPostId())
                .commentId(notification.getCommentId())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}