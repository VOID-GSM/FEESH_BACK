package com.feesh.main.service;

import com.feesh.main.dto.CategoryResponse;
import com.feesh.main.dto.PostListReponse;
import com.feesh.main.dto.PostSearchResponse;
import com.feesh.main.dto.PostSummaryResponse;
import com.feesh.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import com.feesh.post.entity.Post;
import com.feesh.post.entity.Category;
import java.util.Arrays;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainService {
    private final PostRepository postRepository;

    public PostListReponse getPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);

        List<PostSummaryResponse> summaries = posts.getContent().stream()
                .map(post -> PostSummaryResponse.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .build())
                .toList();

        return PostListReponse.builder()
                .posts(summaries)
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .build();
    }

    public PostListReponse getLatestPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);

        List<PostSummaryResponse> summaries = posts.getContent().stream()
                .map(post -> PostSummaryResponse.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .build())
                .toList();

        return PostListReponse.builder()
                .posts(summaries)
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .build();
    }

    public PostListReponse getPopularPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAllByOrderByLikeCountDesc(pageable);

        List<PostSummaryResponse> summaries = posts.getContent().stream()
                .map(post -> PostSummaryResponse.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .build())
                .toList();
        return PostListReponse.builder()
                .posts(summaries)
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
}