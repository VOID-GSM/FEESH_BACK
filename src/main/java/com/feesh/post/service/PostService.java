package com.feesh.post.service;

import org.springframework.transaction.annotation.Transactional;
import com.feesh.post.dto.PostRequest;
import com.feesh.post.entity.Post;
import com.feesh.post.repository.PostRepository;
import com.feesh.user.entity.User;
import com.feesh.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void createPost(PostRequest request) {
        User author = userRepository.findByEmail("test@test.com")
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .author(author)
                .build();

        postRepository.save(post);
    }

    @Transactional
    public void updatePost(Long postId, PostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        post.update(
                request.getTitle(),
                request.getContent(),
                request.getCategory()
        );
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        postRepository.delete(post);
    }
}