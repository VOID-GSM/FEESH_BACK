package com.feesh.domain.post.entity;

import com.feesh.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private int likeCount;

    @Column(nullable = false)
    private int viewCount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Column(nullable = false)
    private Long price;

    @Column
    private String imageUrl;

    @Builder
    public Post(
            String title,
            String content,
            Category category,
            Long price,
            User author,
            String imageUrl
    ) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.price = price;
        this.author = author;
        this.imageUrl = imageUrl;
        this.likeCount = 0;
        this.viewCount = 0;
        this.createdAt = LocalDateTime.now();
    }

    public void update(
            String title,
            String content,
            Category category,
            Long price,
            String imageUrl
    ) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    public void increaseViewCount() {
        this.viewCount++;
    }
}