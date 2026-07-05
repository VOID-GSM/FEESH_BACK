package com.feesh.post.controller;

import com.feesh.post.dto.PostRequest;
import com.feesh.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    public String createPost(@RequestBody PostRequest request) {
        postService.createPost(request);
        return "게시글 작성 성공";
    }
    @PatchMapping("/post/{postId}")
    public String updatePost(@PathVariable Long postId, @RequestBody PostRequest request) {
        postService.updatePost(postId, request);
        return "게시글 수정 성공";
    }

    @DeleteMapping("/post/{postId}")
    public String deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return "게시글 삭제 성공";
    }
}