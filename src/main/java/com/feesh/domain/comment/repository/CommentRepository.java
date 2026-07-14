package com.feesh.domain.comment.repository;

import com.feesh.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost_IdAndParentIsNullAndIsDeletedFalseOrderByCreatedAtAsc(Long postId);
    List<Comment> findByParent_IdAndIsDeletedFalseOrderByCreatedAtAsc(Long parentId);
    Page<Comment> findByAuthorId(Long authorId, Pageable pageable);
}