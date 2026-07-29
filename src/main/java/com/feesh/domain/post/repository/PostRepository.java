package com.feesh.domain.post.repository;

import com.feesh.domain.main.dto.PostSummaryResponse;
import org.springframework.data.jpa.repository.Query;
import com.feesh.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Post> findAllByOrderByLikeCountDesc(Pageable pageable);

    Page<Post> findByTitleContaining(String keyword, Pageable pageable);

    Page<Post> findByAuthorId(Long authorId, Pageable pageable);

    @Query(value = """
            SELECT new com.feesh.domain.main.dto.PostSummaryResponse(
                p.id, p.title, p.category, p.price, p.content,
                author.nickname, author.profileImageUrl, p.likeCount,
                (SELECT COUNT(c) FROM Comment c WHERE c.post = p AND c.isDeleted = false),
                p.createdAt
            )
            FROM Post p
            JOIN p.author author
            """,
            countQuery = "SELECT COUNT(p) FROM Post p")
    Page<PostSummaryResponse> findPostSummaries(Pageable pageable);
}