package com.feesh.domain.comment.service;

import com.feesh.domain.comment.dto.CommentRequest;
import com.feesh.domain.comment.dto.CommentResponse;
import com.feesh.domain.comment.entity.Comment;
import com.feesh.domain.comment.repository.CommentRepository;
import com.feesh.domain.notification.service.NotificationService;
import com.feesh.global.exception.CustomException;
import com.feesh.global.exception.ErrorCode;
import com.feesh.domain.post.entity.Post;
import com.feesh.domain.post.repository.PostRepository;
import com.feesh.domain.user.entity.User;
import com.feesh.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    // 댓글 작성
    @Transactional
    public CommentResponse createComment(Long postId, Long userId, CommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Comment comment = Comment.createComment(post, author, request.getContent());
        commentRepository.save(comment);

        // 본인 게시글에 본인이 댓글 단 경우는 알림 생성 안 함
        if (!post.getAuthor().getId().equals(userId)) {
            notificationService.createCommentNotification(
                    post.getAuthor().getId(), userId, postId, comment.getId()
            );
        }

        return new CommentResponse(comment);
    }

    // 답글 작성
    @Transactional
    public CommentResponse createReply(Long parentId, Long userId, CommentRequest request) {
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PARENT_COMMENT_NOT_FOUND));

        if (parent.isDeleted()) {
            throw new CustomException(ErrorCode.PARENT_COMMENT_NOT_FOUND);
        }
        if (parent.isReply()) {
            throw new CustomException(ErrorCode.INVALID_REPLY_TARGET);
        }

        User author = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Comment reply = Comment.createReply(parent.getPost(), author, parent, request.getContent());
        commentRepository.save(reply);

        // 원 댓글 작성자 본인이 답글 단 경우는 알림 생성 안 함
        Long parentAuthorId = parent.getAuthor().getId();
        if (!parentAuthorId.equals(userId)) {
            notificationService.createCommentNotification(
                    parentAuthorId, userId, parent.getPost().getId(), reply.getId()
            );
        }

        return new CommentResponse(reply);
    }

    // 댓글 목록 조회
    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long postId) {
        List<Comment> comments = commentRepository
                .findByPost_IdAndParentIsNullAndIsDeletedFalseOrderByCreatedAtAsc(postId);
        List<CommentResponse> result = new ArrayList<>();
        for (Comment comment : comments) {
            result.add(new CommentResponse(comment));
        }
        return result;
    }

    // 답글 목록 조회
    @Transactional(readOnly = true)
    public List<CommentResponse> getReplies(Long parentId) {
        List<Comment> replies = commentRepository
                .findByParent_IdAndIsDeletedFalseOrderByCreatedAtAsc(parentId);
        return replies.stream()
                .map(CommentResponse::new)
                .toList();
    }

    // 댓글/답글 삭제
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        if (!comment.isOwnedBy(userId)) {
            throw new CustomException(ErrorCode.COMMENT_ACCESS_DENIED);
        }
        comment.softDelete();
    }
}