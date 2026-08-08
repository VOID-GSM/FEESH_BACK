package com.feesh.domain.post.controller;

import com.feesh.domain.post.dto.request.PostRequest;
import com.feesh.domain.post.dto.response.PostResponse;
import com.feesh.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping(
            value = "/post",
            consumes = "multipart/form-data"
    )
    public String createPost(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestPart("request") PostRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        postService.createPost(userId, request, image);
        return "게시글 작성 완료";
    }

    @PatchMapping("/{postId}")
    public String updatePost(@PathVariable Long postId, @Valid @RequestBody PostRequest request) {
        postService.updatePost(postId, request);
        return "게시글 수정 완료";
    }

    @DeleteMapping("/{postId}")
    public String deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return "게시글 삭제 완료";
    }

    @GetMapping("/{postId}") // 게시글 조회
    public PostResponse getPost(
            @PathVariable Long postId,
            @AuthenticationPrincipal Long userId
    ) {
        return postService.getPostDetail(postId, userId);
    }
}