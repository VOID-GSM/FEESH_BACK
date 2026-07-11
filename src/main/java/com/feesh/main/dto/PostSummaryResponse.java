package com.feesh.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PostSummaryResponse {
    private Long id;
    private String title;
}