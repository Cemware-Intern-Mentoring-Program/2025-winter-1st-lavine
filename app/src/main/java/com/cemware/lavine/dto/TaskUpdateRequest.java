package com.cemware.lavine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskUpdateRequest(
        @NotBlank(message = "할 일 제목은 필수입니다.")
        @Size(max = 50, message = "할 일 제목은 50자 이하여야 합니다.")
        String title
) {
}
