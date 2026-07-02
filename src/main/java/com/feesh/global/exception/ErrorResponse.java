package com.feesh.global.exception;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class ErrorResponse {

    private final String errorCode;
    private final String message;

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .errorCode(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }
}