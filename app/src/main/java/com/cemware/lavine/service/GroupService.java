package com.cemware.lavine.service;

import com.cemware.lavine.dto.GroupCreateRequest;
import com.cemware.lavine.dto.GroupResponse;
import com.cemware.lavine.dto.GroupUpdateRequest;
import com.cemware.lavine.dto.TaskResponse;
import com.cemware.lavine.entity.Group;
import com.cemware.lavine.entity.Task;
import com.cemware.lavine.entity.User;
import com.cemware.lavine.exception.ErrorMessage;
import com.cemware.lavine.repository.GroupRepository;
import com.cemware.lavine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

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
    public GroupResponse createGroup(GroupCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.userNotFound(request.getUserId())));
        
        Group group = new Group(user, request.getName());
        Group savedGroup = groupRepository.save(group);
        
        return GroupResponse.builder()
                .id(savedGroup.getId())
                .name(savedGroup.getName())
                .userId(savedGroup.getUser().getId())
                .tasks(savedGroup.getTasks().stream()
                        .map(this::toTaskResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public GroupResponse updateGroup(Long id, GroupUpdateRequest request) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.groupNotFound(id)));
        group.changeName(request.getName());
        
        return GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .userId(group.getUser().getId())
                .tasks(group.getTasks().stream()
                        .map(this::toTaskResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public void deleteGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.groupNotFound(id)));
        // orphanRemoval = true로 설정되어 있어서 하위 할 일이 자동으로 삭제됩니다.
        groupRepository.delete(group);
    }

    public GroupResponse getGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.groupNotFound(id)));
        
        return GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .userId(group.getUser().getId())
                .tasks(group.getTasks().stream()
                        .map(this::toTaskResponse)
                        .collect(Collectors.toList()))
                .build();
    }
}

