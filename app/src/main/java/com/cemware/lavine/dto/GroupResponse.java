package com.cemware.lavine.dto;

import java.util.List;

public record GroupResponse(
        Long id,
        String name,
        Long userId,
        List<TaskResponse> tasks
) {
}
