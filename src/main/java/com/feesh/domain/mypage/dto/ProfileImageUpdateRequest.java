package com.feesh.domain.mypage.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileImageUpdateRequest {
    @NotNull
    private String image;
}
