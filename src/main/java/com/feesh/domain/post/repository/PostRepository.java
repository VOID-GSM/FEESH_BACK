package com.feesh.domain.post.repository;

import com.feesh.domain.main.dto.PostSummaryResponse;
import com.feesh.domain.post.entity.Category;
import com.feesh.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByAuthorId(Long authorId, Pageable pageable);

    void deleteAllByAuthor_Id(Long authorId);

    @Query(value = """
        SELECT new com.feesh.domain.main.dto.PostSummaryResponse(
            p.id, p.title, p.category, p.price, p.content,
            author.nickname, author.profileImageUrl, p.likeCount,
            (SELECT COUNT(c) FROM Comment c WHERE c.post = p AND c.isDeleted = false),
            p.createdAt,
            CASE WHEN EXISTS (
                 SELECT 1 FROM PostLike pl WHERE pl.post = p AND pl.user.id = :userId
            ) THEN true ELSE false END,
            p.viewCount
        )
        FROM Post p
        JOIN p.author author
        """,
            countQuery = "SELECT COUNT(p) FROM Post p")
    Page<PostSummaryResponse> findPostSummaries(Pageable pageable, @Param("userId") Long userId);

    @Query(value = """
        SELECT new com.feesh.domain.main.dto.PostSummaryResponse(
            p.id, p.title, p.category, p.price, p.content,
            author.nickname, author.profileImageUrl, p.likeCount,
            (SELECT COUNT(c) FROM Comment c WHERE c.post = p AND c.isDeleted = false),
            p.createdAt,
            CASE WHEN EXISTS (
                 SELECT 1 FROM PostLike pl WHERE pl.post = p AND pl.user.id = :userId
            ) THEN true ELSE false END,
            p.viewCount
        )
        FROM Post p
        JOIN p.author author
        WHERE (:category IS NULL OR p.category = :category)
        ORDER BY p.createdAt DESC
        """,
            countQuery = """
            SELECT COUNT(p) FROM Post p
            WHERE (:category IS NULL OR p.category = :category)
            """)
    Page<PostSummaryResponse> findLatestPostSummaries(
            @Param("category") Category category,
            Pageable pageable,
            @Param("userId") Long userId);

    @Query(value = """
        SELECT new com.feesh.domain.main.dto.PostSummaryResponse(
            p.id, p.title, p.category, p.price, p.content,
            author.nickname, author.profileImageUrl, p.likeCount,
            (SELECT COUNT(c) FROM Comment c WHERE c.post = p AND c.isDeleted = false),
            p.createdAt,
            CASE WHEN EXISTS (
                 SELECT 1 FROM PostLike pl WHERE pl.post = p AND pl.user.id = :userId
            ) THEN true ELSE false END,
            p.viewCount
        )
        FROM Post p
        JOIN p.author author
        WHERE (:category IS NULL OR p.category = :category)
        ORDER BY p.likeCount DESC
        """,
            countQuery = """
            SELECT COUNT(p) FROM Post p
            WHERE (:category IS NULL OR p.category = :category)
            """)
    Page<PostSummaryResponse> findPopularPostSummaries(
            @Param("category") Category category,
            Pageable pageable,
            @Param("userId") Long userId);

    @Query(value = """
    SELECT new com.feesh.domain.main.dto.PostSummaryResponse(
        p.id, p.title, p.category, p.price, p.content,
        author.nickname, author.profileImageUrl, p.likeCount,
        (SELECT COUNT(c) FROM Comment c WHERE c.post = p AND c.isDeleted = false),
        p.createdAt,
        CASE WHEN EXISTS (
             SELECT 1 FROM PostLike pl WHERE pl.post = p AND pl.user.id = :userId
        ) THEN true ELSE false END,
        p.viewCount
    )
    FROM Post p
    JOIN p.author author
    WHERE (:keyword IS NULL OR p.title LIKE CONCAT('%', :keyword, '%') OR author.nickname LIKE CONCAT('%', :keyword, '%'))
    AND (:category IS NULL OR p.category = :category)
    ORDER BY p.createdAt DESC
    """,
            countQuery = """
            SELECT COUNT(p) FROM Post p
            JOIN p.author author
            WHERE (:keyword IS NULL OR p.title LIKE CONCAT('%', :keyword, '%') OR author.nickname LIKE CONCAT('%', :keyword, '%'))
            AND (:category IS NULL OR p.category = :category)
            """)
    Page<PostSummaryResponse> searchPostSummariesByTitle(
            @Param("keyword") String keyword,
            @Param("category") Category category,
            Pageable pageable,
            @Param("userId") Long userId);
}
