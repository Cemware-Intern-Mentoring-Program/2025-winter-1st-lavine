package com.cemware.lavine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskCreateRequest(
        @NotBlank(message = "할 일 제목은 필수입니다.")
        @Size(max = 50, message = "할 일 제목은 50자 이하여야 합니다.")
        String title,
        
        @NotNull(message = "그룹 ID는 필수입니다.")
        Long groupId,
        
        @NotNull(message = "유저 ID는 필수입니다.")
        Long userId
) {
}
