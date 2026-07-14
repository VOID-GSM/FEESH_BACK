package com.feesh.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckEmailResponse {
    private boolean duplicated;
    private String message;
}