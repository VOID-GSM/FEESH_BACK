package com.feesh.domain.post.service;

import com.feesh.domain.like.repository.PostLikeRepository;
import com.feesh.domain.post.dto.request.PostRequest;
import com.feesh.domain.post.dto.response.PostResponse;
import com.feesh.domain.post.entity.Post;
import com.feesh.domain.post.repository.PostRepository;
import com.feesh.domain.user.entity.User;
import com.feesh.domain.user.repository.UserRepository;
import com.feesh.global.exception.CustomException;
import com.feesh.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public void createPost(Long userId, PostRequest request) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .price(request.getPrice())
                .author(author)
                .build();

        postRepository.save(post);
    }

    @Transactional
    public void updatePost(Long postId, PostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        post.update(
                request.getTitle(),
                request.getContent(),
                request.getCategory(),
                request.getPrice()
        );
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (!post.getAuthor().getId().equals(userId)) {
            throw new CustomException(ErrorCode.POST_ACCESS_DENIED);
        }

        postRepository.delete(post);
    }

    @Transactional
    public PostResponse getPostDetail(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        post.increaseViewCount();

        boolean liked = (userId != null)
                && postLikeRepository.existsByPost_IdAndUser_Id(postId, userId);

        return new PostResponse(post, liked);
    }
}