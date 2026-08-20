package com.feesh.domain.post.repository;

import com.feesh.domain.post.entity.PostView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostViewRepository extends JpaRepository<PostView, Long> {

    boolean existsByPost_IdAndUser_Id(Long postId, Long userId);

    void deleteAllByPost_Id(Long postId);

    void deleteAllByPost_Author_Id(Long userId);
}