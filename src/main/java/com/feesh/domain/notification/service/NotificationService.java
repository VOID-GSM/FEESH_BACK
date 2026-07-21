package com.feesh.domain.notification.service;

import com.feesh.domain.notification.dto.NotificationResponseDto;
import com.feesh.domain.notification.entity.Notification;
import com.feesh.domain.notification.entity.NotificationType;
import com.feesh.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<NotificationResponseDto> getCommentAlarms(Long userId) {
        return notificationRepository
                .findByReceiverIdAndTypeOrderByCreatedAtDesc(userId, NotificationType.COMMENT)
                .stream()
                .map(NotificationResponseDto::from)
                .toList();
    }

    public List<NotificationResponseDto> getLikeAlarms(Long userId) {
        return notificationRepository
                .findByReceiverIdAndTypeOrderByCreatedAtDesc(userId, NotificationType.LIKE)
                .stream()
                .map(NotificationResponseDto::from)
                .toList();
    }

    // 댓글 알림 생성
    @Transactional
    public void createCommentNotification(Long receiverId, Long senderId, Long postId, Long commentId) {
        Notification notification = Notification.builder()
                .receiverId(receiverId)
                .senderId(senderId)
                .type(NotificationType.COMMENT)
                .postId(postId)
                .commentId(commentId)
                .build();
        notificationRepository.save(notification);
    }

    // 좋아요 알림 생성
    @Transactional
    public void createLikeNotification(Long receiverId, Long senderId, Long postId) {
        Notification notification = Notification.builder()
                .receiverId(receiverId)
                .senderId(senderId)
                .type(NotificationType.LIKE)
                .postId(postId)
                .commentId(null)
                .build();
        notificationRepository.save(notification);
    }
}