package com.cemware.lavine.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private boolean done;
    private Long groupId;
    private Long userId;
}

