package com.cemware.lavine.service;

import com.cemware.lavine.dto.GroupCreateRequest;
import com.cemware.lavine.dto.GroupResponse;
import com.cemware.lavine.dto.GroupUpdateRequest;
import com.cemware.lavine.entity.Group;
import com.cemware.lavine.entity.User;
import com.cemware.lavine.exception.ResourceNotFoundException;
import com.cemware.lavine.mapper.TaskMapper;
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

    @Transactional
    public GroupResponse createGroup(GroupCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("유저", request.userId()));
        
        Group group = new Group(user, request.name());
        Group savedGroup = groupRepository.save(group);
        
        return new GroupResponse(
                savedGroup.getId(),
                savedGroup.getName(),
                savedGroup.getUser().getId(),
                savedGroup.getTasks().stream()
                        .map(TaskMapper::toTaskResponse)
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public GroupResponse updateGroup(Long id, GroupUpdateRequest request) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("그룹", id));
        group.changeName(request.name());
        
        return new GroupResponse(
                group.getId(),
                group.getName(),
                group.getUser().getId(),
                group.getTasks().stream()
                        .map(TaskMapper::toTaskResponse)
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public void deleteGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("그룹", id));
        // orphanRemoval = true로 설정되어 있어서 하위 할 일이 자동으로 삭제됩니다.
        groupRepository.delete(group);
    }

    public GroupResponse getGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("그룹", id));
        
        return new GroupResponse(
                group.getId(),
                group.getName(),
                group.getUser().getId(),
                group.getTasks().stream()
                        .map(TaskMapper::toTaskResponse)
                        .collect(Collectors.toList())
        );
    }
}

