package com.feesh.domain.post.controller;

import com.feesh.domain.post.dto.request.PostRequest;
import com.feesh.domain.post.dto.response.PostResponse;
import com.feesh.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    public String createPost(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody PostRequest request
    ) {
        postService.createPost(userId, request);
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

    @GetMapping("/{postId}") //게시글 조회
    public PostResponse getPost(@PathVariable Long postId) {
        return postService.getPostDetail(postId);
    }

}