package com.cemware.lavine.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateStatusRequest {
    @NotNull(message = "완료 상태는 필수입니다.")
    private Boolean done;
}

