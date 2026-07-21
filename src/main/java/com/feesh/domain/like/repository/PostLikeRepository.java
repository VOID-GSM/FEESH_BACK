package com.feesh.domain.like.repository;

import com.feesh.domain.like.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByPostIdAndUserId(Long postId, Long userId);

    java.util.Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);
}