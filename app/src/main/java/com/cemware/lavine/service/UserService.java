package com.cemware.lavine.service;

import com.cemware.lavine.dto.GroupResponse;
import com.cemware.lavine.dto.TaskResponse;
import com.cemware.lavine.dto.UserCreateRequest;
import com.cemware.lavine.dto.UserResponse;
import com.cemware.lavine.dto.UserUpdateRequest;
import com.cemware.lavine.entity.Task;
import com.cemware.lavine.entity.User;
import com.cemware.lavine.exception.ErrorMessage;
import com.cemware.lavine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

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
    public UserResponse createUser(UserCreateRequest request) {
        User user = new User(request.getName());
        User savedUser = userRepository.save(user);
        return UserResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .build();
    }

    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.userNotFound(id)));
        user.changeName(request.getName());
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    @Transactional
    public void deleteUser(Long id) { // 하위그룹, 과제도 cascade로 자동 삭제
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.userNotFound(id)));
        userRepository.delete(user);
    }

    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.userNotFound(id)));
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public List<GroupResponse> getUserGroups(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.userNotFound(userId)));
        
        return user.getGroups().stream()
                .map(group -> GroupResponse.builder()
                        .id(group.getId())
                        .name(group.getName())
                        .userId(group.getUser().getId())
                        .tasks(group.getTasks().stream()
                                .map(this::toTaskResponse)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
}

