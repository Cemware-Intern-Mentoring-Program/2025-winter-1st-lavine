package com.cemware.lavine.dto;

public record TaskResponse(
        Long id,
        String title,
        boolean done,
        Long groupId,
        Long userId
) {
}
