package com.cemware.lavine.service;

import com.cemware.lavine.dto.TaskCreateRequest;
import com.cemware.lavine.dto.TaskResponse;
import com.cemware.lavine.dto.TaskUpdateRequest;
import com.cemware.lavine.dto.TaskUpdateStatusRequest;
import com.cemware.lavine.entity.Group;
import com.cemware.lavine.entity.Task;
import com.cemware.lavine.entity.User;
import com.cemware.lavine.exception.ErrorMessage;
import com.cemware.lavine.repository.GroupRepository;
import com.cemware.lavine.repository.TaskRepository;
import com.cemware.lavine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    private TaskResponse toTaskResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .done(task.isDone())
                .groupId(task.getGroup().getId())
                .userId(task.getUser().getId())
                .build();
    }

    @Transactional
    public TaskResponse createTask(TaskCreateRequest request) {
        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.groupNotFound(request.getGroupId())));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.userNotFound(request.getUserId())));
        
        Task task = Task.builder()
                .title(request.getTitle())
                .group(group)
                .user(user)
                .build();
        
        Task savedTask = taskRepository.save(task);
        return toTaskResponse(savedTask);
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskUpdateRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.taskNotFound(id)));
        
        task.changeTitle(request.getTitle());
        return toTaskResponse(task);
    }

    @Transactional
    public TaskResponse updateTaskStatus(Long id, TaskUpdateStatusRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.taskNotFound(id)));
        
        if (request.getDone()) { 
            task.markDone();
            return toTaskResponse(task);
        }
        
        task.markUndone();
        return toTaskResponse(task);
    }

    @Transactional
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.taskNotFound(id)));
        taskRepository.delete(task);
    }

    public TaskResponse getTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.taskNotFound(id)));
        return toTaskResponse(task);
    }
}

