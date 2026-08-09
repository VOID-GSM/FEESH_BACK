package com.feesh.domain.post.controller;

import com.feesh.domain.post.dto.request.PostRequest;
import com.feesh.domain.post.dto.response.PostResponse;
import com.feesh.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @PostMapping(
            value = "/post",
            consumes = "multipart/form-data"
    )
    public String createPost(
            @AuthenticationPrincipal Long userId,
            @RequestPart("request") String requestJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws JsonProcessingException {

        PostRequest request = objectMapper.readValue(
                requestJson,
                PostRequest.class
        );

        Set<ConstraintViolation<PostRequest>> violations =
                validator.validate(request);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }   

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