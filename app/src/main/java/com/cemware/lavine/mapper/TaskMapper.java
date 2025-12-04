package com.cemware.lavine.mapper;

import com.cemware.lavine.dto.TaskResponse;
import com.cemware.lavine.entity.Task;

public class TaskMapper {
    
    private TaskMapper() {
        // 유틸리티 클래스이므로 인스턴스화 방지
    }
    
    public static TaskResponse toTaskResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.isDone(),
                task.getGroup().getId(),
                task.getUser().getId()
        );
    }
}

