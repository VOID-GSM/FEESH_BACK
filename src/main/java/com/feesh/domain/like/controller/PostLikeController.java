package com.feesh.domain.like.controller;

import com.feesh.domain.like.service.PostLikeService;
import com.feesh.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostLikeController {

    private final PostLikeService postLikeService;


    // 좋아요
    @PostMapping("/{postId}/like")
    public String like(@PathVariable Long postId) {

        Long userId = SecurityUtil.getCurrentUserId();

        postLikeService.like(postId, userId);
        return "좋아요 완료";

    }


    // 좋아요 취소
    @DeleteMapping("/{postId}/like")
    public String unlike(@PathVariable Long postId) {

        Long userId = SecurityUtil.getCurrentUserId();

        postLikeService.unlike(postId, userId);
        return "좋아요 취소";
    }
}