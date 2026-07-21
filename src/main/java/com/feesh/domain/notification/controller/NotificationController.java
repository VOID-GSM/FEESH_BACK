package com.feesh.domain.notification.controller;

import com.feesh.global.util.SecurityUtil;
import com.feesh.domain.notification.dto.NotificationResponseDto;
import com.feesh.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/alarm")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/comment")
    public ResponseEntity<List<NotificationResponseDto>> getCommentAlarms() {
        Long userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(notificationService.getCommentAlarms(userId));
    }

    @GetMapping("/like")
    public ResponseEntity<List<NotificationResponseDto>> getLikeAlarms() {
        Long userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(notificationService.getLikeAlarms(userId));
    }
}