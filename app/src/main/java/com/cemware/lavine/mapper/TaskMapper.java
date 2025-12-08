package com.cemware.lavine.mapper;

import com.cemware.lavine.dto.TaskResponse;
import com.cemware.lavine.entity.Task;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TaskMapper {
    
    public TaskResponse toTaskResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.isDone(),
                task.getGroup().getId(),
                task.getUser().getId()
        );
    }
}

