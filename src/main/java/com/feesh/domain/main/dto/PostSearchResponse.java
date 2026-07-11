package com.feesh.domain.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PostSearchResponse {
    private List<PostSummaryResponse> posts;
    private int totalPages;
    private long totalElements;
}
