package com.feesh.domain.comment.entity;

import com.feesh.domain.post.entity.Post;
import com.feesh.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    // 답글이면 부모 댓글이 들어감. 그냥 댓글이면 null
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private boolean isDeleted;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Comment(Post post, User author, Comment parent, String content) {
        this.post = post;
        this.author = author;
        this.parent = parent;
        this.content = content;
        this.isDeleted = false;
        this.createdAt = LocalDateTime.now();
    }

    public static Comment createComment(Post post, User author, String content) {
        return new Comment(post, author, null, content);
    }

    public static Comment createReply(Post post, User author, Comment parent, String content) {
        return new Comment(post, author, parent, content);
    }

    public boolean isReply() {
        return this.parent != null;
    }

    public boolean isOwnedBy(Long userId) {
        return this.author.getId().equals(userId);
    }

    public void softDelete() {
        this.isDeleted = true;
    }
}