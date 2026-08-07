package com.feesh.domain.post.service;

import com.feesh.domain.like.repository.PostLikeRepository;
import com.feesh.domain.post.dto.request.PostRequest;
import com.feesh.domain.post.dto.response.PostResponse;
import com.feesh.domain.post.entity.Post;
import com.feesh.domain.post.entity.PostView;
import com.feesh.domain.post.repository.PostRepository;
import com.feesh.domain.post.repository.PostViewRepository;
import com.feesh.domain.user.entity.User;
import com.feesh.domain.user.repository.UserRepository;
import com.feesh.global.exception.CustomException;
import com.feesh.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.feesh.domain.comment.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final PostViewRepository postViewRepository;

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
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        postViewRepository.deleteAllByPost_Id(postId);
        postLikeRepository.deleteAllByPost_Id(postId);
        commentRepository.deleteAllByParentIsNotNullAndPost_Id(postId);
        commentRepository.deleteAllByPost_Id(postId);

        postRepository.delete(post);
    }

    @Transactional
    public PostResponse getPostDetail(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        increaseViewCountIfFirstView(post, userId);

        boolean liked = postLikeRepository.existsByPost_IdAndUser_Id(postId, userId);

        return new PostResponse(post, liked);
    }

    private void increaseViewCountIfFirstView(Post post, Long userId) {
        // 대부분의 재조회 요청은 여기서 걸러짐 (INSERT 시도 자체를 안 함)
        if (postViewRepository.existsByPost_IdAndUser_Id(post.getId(), userId)) {
            return;
        }

        User viewer = userRepository.getReferenceById(userId); // 프록시 참조, 쿼리 안 나감

        try {
            postViewRepository.save(PostView.builder()
                    .post(post)
                    .user(viewer)
                    .build());
            post.increaseViewCount();
        } catch (DataIntegrityViolationException e) {
            // 동시 요청 두 개가 거의 동시에 들어와 existsBy 체크를 둘 다 통과한 경우
            // (post_id, user_id) 유니크 제약 위반으로 하나는 여기서 걸러짐
        }
    }
}