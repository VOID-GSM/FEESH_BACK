package com.feesh.domain.main.controller;

import com.feesh.domain.main.dto.CategoryResponse;
import com.feesh.domain.main.dto.PostListResponse;
import com.feesh.domain.main.dto.PostSearchResponse;
import com.feesh.domain.main.service.MainService;
import com.feesh.domain.post.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.data.domain.Sort;
import com.feesh.global.util.SecurityUtil;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;

    @GetMapping("/posts")
    public PostListResponse getPosts(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long userId = SecurityUtil.getCurrentUserIdOrNull();
        return mainService.getPosts(pageable, userId);
    }

    @GetMapping("/posts/latest")
    public PostListResponse getLatestPosts(
            @RequestParam(required = false) Category category,
            @PageableDefault Pageable pageable) {
        Long userId = SecurityUtil.getCurrentUserIdOrNull();
        return mainService.getLatestPosts(category, pageable, userId);
    }

    @GetMapping("/posts/popular")
    public PostListResponse getPopularPosts(
            @RequestParam(required = false) Category category,
            @PageableDefault Pageable pageable) {
        Long userId = SecurityUtil.getCurrentUserIdOrNull();
        return mainService.getPopularPosts(category, pageable, userId);
    }

    @GetMapping("/search")
    public PostSearchResponse search(
            @RequestParam String keyword,
            @PageableDefault Pageable pageable) {
        return mainService.searchPosts(keyword, pageable);
    }

    @GetMapping("/categories")
    public List<CategoryResponse> getCategories() {
        return mainService.getCategories();
    }
}
