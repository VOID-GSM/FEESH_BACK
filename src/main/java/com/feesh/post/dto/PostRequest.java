package com.feesh.post.dto;

import com.feesh.post.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostRequest {

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String content;

    @NotNull(message = "카테고리는 필수 입력 값입니다.")
    private Category category;

}