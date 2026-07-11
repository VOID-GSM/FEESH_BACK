package com.feesh.domain.post.repository;

import com.feesh.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Post> findAllByOrderByLikeCountDesc(Pageable pageable);
    Page<Post> findByTitleContaining(String keyword, Pageable pageable);
}