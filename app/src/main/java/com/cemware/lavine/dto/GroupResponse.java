package com.cemware.lavine.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GroupResponse {
    private Long id;
    private String name;
    private Long userId;
    private List<TaskResponse> tasks;
}

