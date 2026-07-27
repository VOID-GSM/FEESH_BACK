package com.feesh.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerifyEmailCodeResponse {

    private boolean verified;
    private String message;
}