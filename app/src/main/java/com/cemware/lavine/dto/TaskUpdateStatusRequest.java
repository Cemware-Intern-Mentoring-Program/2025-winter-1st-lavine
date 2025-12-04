package com.cemware.lavine.dto;

import jakarta.validation.constraints.NotNull;

public record TaskUpdateStatusRequest(
        @NotNull(message = "완료 상태는 필수입니다.")
        Boolean done
) {
}
