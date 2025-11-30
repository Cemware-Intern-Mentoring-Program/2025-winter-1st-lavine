package com.cemware.lavine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateRequest {
    @NotBlank(message = "할 일 제목은 필수입니다.")
    @Size(max = 50, message = "할 일 제목은 50자 이하여야 합니다.")
    private String title;

    @NotNull(message = "그룹 ID는 필수입니다.")
    private Long groupId;

    @NotNull(message = "유저 ID는 필수입니다.")
    private Long userId;
}

