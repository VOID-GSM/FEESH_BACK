package com.feesh.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckEmailResponse {

    private boolean duplicated;
    private String message;

}