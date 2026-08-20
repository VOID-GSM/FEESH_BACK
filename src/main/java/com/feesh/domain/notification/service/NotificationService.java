package com.feesh.domain.notification.service;

import com.feesh.domain.comment.entity.Comment;
import com.feesh.domain.comment.repository.CommentRepository;
import com.feesh.domain.notification.dto.NotificationResponseDto;
import com.feesh.domain.notification.entity.Notification;
import com.feesh.domain.notification.entity.NotificationType;
import com.feesh.domain.notification.repository.NotificationRepository;
import com.feesh.domain.user.entity.User;
import com.feesh.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public List<NotificationResponseDto> getCommentAlarms(Long userId) {
        List<Notification> notifications = notificationRepository
                .findByReceiverIdAndTypeOrderByCreatedAtDesc(userId, NotificationType.COMMENT);

        return toResponseDtos(notifications);
    }

    public List<NotificationResponseDto> getLikeAlarms(Long userId) {
        List<Notification> notifications = notificationRepository
                .findByReceiverIdAndTypeOrderByCreatedAtDesc(userId, NotificationType.LIKE);

        return toResponseDtos(notifications);
    }

    // 알림 목록의 senderId, commentId를 모아 각각 한 번에 조회한 뒤 DTO로 변환 (N+1 방지)
    private List<NotificationResponseDto> toResponseDtos(List<Notification> notifications) {
        List<Long> senderIds = notifications.stream()
                .map(Notification::getSenderId)
                .distinct()
                .toList();

        Map<Long, User> senderMap = userRepository.findAllById(senderIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        List<Long> commentIds = notifications.stream()
                .map(Notification::getCommentId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<Long, Comment> commentMap = commentRepository.findAllById(commentIds).stream()
                .collect(Collectors.toMap(Comment::getId, Function.identity()));

        return notifications.stream()
                .map(notification -> NotificationResponseDto.from(
                        notification,
                        senderMap.get(notification.getSenderId()),
                        commentMap.get(notification.getCommentId())
                ))
                .toList();
    }

    // 댓글 알림 생성
    @Transactional
    public void createCommentNotification(Long receiverId, Long senderId, Long postId, Long commentId) {
        if (receiverId.equals(senderId)) {
            return;
        }
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
        if (receiverId.equals(senderId)) {
            return;
        }
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