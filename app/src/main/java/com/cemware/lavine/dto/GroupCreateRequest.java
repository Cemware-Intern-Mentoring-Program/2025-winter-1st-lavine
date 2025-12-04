package com.cemware.lavine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GroupCreateRequest(
        @NotNull(message = "유저 ID는 필수입니다.")
        Long userId,
        
        @NotBlank(message = "그룹 이름은 필수입니다.")
        @Size(max = 50, message = "그룹 이름은 50자 이하여야 합니다.")
        String name
) {
}
