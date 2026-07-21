package com.feesh.domain.like.service;

import com.feesh.domain.like.entity.PostLike;
import com.feesh.domain.like.repository.PostLikeRepository;
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
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    // 좋아요 추가
    @Transactional
    public void like(Long postId, Long userId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));


        // 이미 좋아요 눌렀는지 확인
        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new CustomException(ErrorCode.LIKE_ALREADY_EXISTS);
        }


        // 좋아요 저장
        PostLike postLike = new PostLike(post, user);
        postLikeRepository.save(postLike);


        // 게시글 좋아요 수 증가
        post.increaseLikeCount();
    }


    // 좋아요 취소
    @Transactional
    public void unlike(Long postId, Long userId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));


        // 좋아요 안 했는데 취소하는 경우
        if (!postLikeRepository.existsByPost_IdAndUser_Id(postId, userId)) {
            throw new CustomException(ErrorCode.LIKE_NOT_FOUND);
        }


        // 좋아요 삭제
        postLikeRepository.deleteByPost_IdAndUser_Id(postId, userId);


        // 게시글 좋아요 수 감소
        post.decreaseLikeCount();
    }
}