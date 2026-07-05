package com.feesh.post.dto;

import com.feesh.post.entity.Category;
import lombok.Getter;

@Getter
public class PostRequest {

    private String title;
    private String content;
    private Category category;

}