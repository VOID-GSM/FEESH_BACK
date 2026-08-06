package com.feesh.domain.mypage.controller;

import com.feesh.domain.mypage.dto.ImageUploadResponse;
import com.feesh.domain.mypage.dto.MyCommentResponse;
import com.feesh.domain.mypage.dto.MyFeedResponse;
import com.feesh.domain.mypage.service.MyPageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.feesh.domain.mypage.dto.ProfileImageUpdateRequest;


@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    @PatchMapping("/update")
    public ResponseEntity<Void> updateProfileImage(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody ProfileImageUpdateRequest request
    ) {
        myPageService.updateProfileImage(userId, request.getImage());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/image")
    public ResponseEntity<ImageUploadResponse> uploadProfileImage(
            @AuthenticationPrincipal Long userId,
            @RequestParam("image") MultipartFile image) {
        String imageUrl = myPageService.uploadProfileImage(image);
        return ResponseEntity.ok(new ImageUploadResponse(imageUrl));
    }

    @GetMapping("/feed")
    public Page<MyFeedResponse> getMyFeed(
            @AuthenticationPrincipal Long userId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return myPageService.getMyFeed(userId, pageable);
    }

    @GetMapping("/comments")
    public Page<MyCommentResponse> getMyComments(
            @AuthenticationPrincipal Long userId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return myPageService.getMyComments(userId, pageable);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        myPageService.logout();
        return ResponseEntity.ok().build();
    }
}
