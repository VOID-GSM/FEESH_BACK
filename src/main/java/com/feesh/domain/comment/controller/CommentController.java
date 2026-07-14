package com.feesh.domain.comment.controller;

import com.feesh.domain.comment.dto.CommentRequest;
import com.feesh.domain.comment.dto.CommentResponse;
import com.feesh.domain.comment.service.CommentService;
import com.feesh.global.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/view")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/{postId}/comments")
    public CommentResponse createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest request
    ) {
        Long userId = SecurityUtil.getCurrentUserId();
        return commentService.createComment(postId, userId, request);
    }

    // 댓글 조회
    @GetMapping("/{postId}/comments")
    public List<CommentResponse> getComments(@PathVariable Long postId) {
        return commentService.getComments(postId);
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public String deleteComment(@PathVariable Long commentId) {
        Long userId = SecurityUtil.getCurrentUserId();
        commentService.deleteComment(commentId, userId);
        return "댓글 삭제 성공";
    }

    // 답글 작성
    @PostMapping("/comments/{commentId}/replies")
    public CommentResponse createReply(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request
    ) {
        Long userId = SecurityUtil.getCurrentUserId();
        return commentService.createReply(commentId, userId, request);
    }

    // 답글 조회
    @GetMapping("/comments/{commentId}/replies")
    public List<CommentResponse> getReplies(@PathVariable Long commentId) {
        return commentService.getReplies(commentId);
    }

    // 답글 삭제
    @DeleteMapping("/replies/{replyId}")
    public String deleteReply(@PathVariable Long replyId) {
        Long userId = SecurityUtil.getCurrentUserId();
        commentService.deleteComment(replyId, userId);
        return "답글 삭제 성공";
    }
}