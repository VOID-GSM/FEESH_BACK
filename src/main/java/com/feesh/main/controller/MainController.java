package com.feesh.main.controller;

import com.feesh.main.dto.CategoryResponse;
import com.feesh.main.dto.PostListReponse;
import com.feesh.main.dto.PostSearchResponse;
import com.feesh.main.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;

    @GetMapping("/posts")
    public PostListReponse getPosts(@PageableDefault Pageable pageable) {
        return mainService.getPosts(pageable);
    }

    @GetMapping("/posts/latest")
    public PostListReponse getPostsLat(@PageableDefault Pageable pageable) {
        return mainService.getLatestPosts(pageable);
    }

    @GetMapping("/posts/popular")
    public PostListReponse getPopularPosts(@PageableDefault Pageable pageable) {
        return mainService.getPopularPosts(pageable);
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
