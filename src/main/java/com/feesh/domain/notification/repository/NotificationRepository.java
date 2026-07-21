package com.feesh.domain.notification.repository;

import com.feesh.domain.notification.entity.Notification;
import com.feesh.domain.notification.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByReceiverIdAndTypeOrderByCreatedAtDesc(Long receiverId, NotificationType type);
}