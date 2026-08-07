package com.feesh.domain.mypage.service;

import com.feesh.domain.mypage.dto.MyFeedResponse;
import com.feesh.domain.post.repository.PostRepository;
import com.feesh.domain.user.entity.User;
import com.feesh.domain.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.base-url}")
    private String baseUrl;

    public Page<MyFeedResponse> getMyFeed(Long userId, Pageable pageable) {
        return postRepository.findByAuthorId(userId, pageable)
                .map(post -> MyFeedResponse.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .likeCount(post.getLikeCount())
                        .createdAt(post.getCreatedAt())
                        .build());
    }

    @Transactional
    public void updateProfileImage(Long userId, String imageUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        user.updateProfileImage(imageUrl);
    }

    @Transactional
    public String uploadProfileImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("이미지 파일이 비어있습니다.");
        }
        String contentType = image.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }
        String ext = StringUtils.getFilenameExtension(image.getOriginalFilename());
        String savedName = UUID.randomUUID() + (ext != null ? "." + ext : "");
        try {
            Path savePath = Paths.get(uploadDir, savedName);
            Files.createDirectories(savePath.getParent());
            image.transferTo(savePath);
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
        return baseUrl + "/uploads/" + savedName;
    }

    public void logout() {
    }

}
