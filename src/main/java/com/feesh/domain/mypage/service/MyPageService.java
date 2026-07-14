package com.feesh.domain.mypage.service;

import com.feesh.domain.mypage.dto.MyCommentResponse;
import com.feesh.domain.mypage.dto.MyFeedResponse;
import com.feesh.domain.post.repository.PostRepository;
import com.feesh.domain.comment.repository.CommentRepository;
import com.feesh.domain.user.repository.UserRepository;

import org.springframework.web.multipart.MultipartFile;
import com.feesh.domain.user.entity.User;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public Page<MyFeedResponse> getMyFeed(Long userId, Pageable pageable) {
        return postRepository.findByAuthorId(userId, pageable)
                .map(post -> MyFeedResponse.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .likeCount(post.getLikeCount())
                        .createdAt(post.getCreatedAt())
                        .build());
    }

    public Page<MyCommentResponse> getMyComments(Long userId, Pageable pageable) {
        return commentRepository.findByAuthorId(userId, pageable)
                .map(comment -> MyCommentResponse.builder()
                        .commentId(comment.getId())
                        .postId(comment.getPost().getId())
                        .comment(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build());
    }

    @Transactional
    public void updateProfileImage(Long userId, MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("업로드할 이미지 파일이 비어있습니다.");
        }

        String contentType = image.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        String originalFilename = image.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String storedFileName = UUID.randomUUID() + extension;
        String uploadDir = "uploads/profile/";

        try {
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            image.transferTo(new File(uploadDir + storedFileName));
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 실패", e);
        }

        user.updateProfileImage("/uploads/profile/" + storedFileName);
    }

    public void logout() {
    }
}
