package com.feesh.domain.main.service;

import com.feesh.domain.main.dto.CategoryResponse;
import com.feesh.domain.main.dto.PostListResponse;
import com.feesh.domain.main.dto.PostSearchResponse;
import com.feesh.domain.main.dto.PostSummaryResponse;
import com.feesh.domain.post.entity.Category;
import com.feesh.domain.post.entity.Post;
import com.feesh.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import java.util.Arrays;
import java.util.List;
import java.util.SimpleTimeZone;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainService {
    private final PostRepository postRepository;

    public PostListResponse getPosts(Pageable pageable, Long userId) {
        Page<PostSummaryResponse> posts = postRepository.findPostSummaries(pageable, userId);
        return PostListResponse.builder()
                .posts(posts.getContent())
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .build();
    }

    public PostListResponse getLatestPosts(Category category, Pageable pageable, Long userId) {
        Page<PostSummaryResponse> posts = postRepository.findLatestPostSummaries(category, pageable, userId);
        return PostListResponse.builder()
                .posts(posts.getContent())
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .build();
    }

    public PostListResponse getPopularPosts (Category category, Pageable pageable, Long userId){
        Page<PostSummaryResponse> posts = postRepository.findPopularPostSummaries(category, pageable, userId);
        return PostListResponse.builder()
                .posts(posts.getContent())
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .build();
    }

    public List<CategoryResponse> getCategories() {
        return Arrays.stream(Category.values())
                .map(category -> CategoryResponse.builder()
                        .name(category.name())
                        .label(category.getLabel())
                        .build())
                .toList();
    }

    public PostSearchResponse searchPosts(String keyword, Pageable pageable) {
        Page<Post> posts = postRepository.findByTitleContaining(keyword, pageable);

        List<PostSummaryResponse> summaries = posts.getContent().stream()
                .map(post -> PostSummaryResponse.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .build())
                .toList();

        return PostSearchResponse.builder()
                .posts(summaries)
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .build();
    }

    private PostListResponse toPostListResponse(Page<Post> posts) {
        List<PostSummaryResponse> summaries = posts.getContent().stream()
                .map(post -> PostSummaryResponse.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .build())
                .toList();

        return PostListResponse.builder()
                .posts(summaries)
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .build();
    }
}