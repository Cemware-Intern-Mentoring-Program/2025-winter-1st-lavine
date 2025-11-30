package com.cemware.lavine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateRequest {
    @NotBlank(message = "할 일 제목은 필수입니다.")
    @Size(max = 50, message = "할 일 제목은 50자 이하여야 합니다.")
    private String title;
}

