package com.cemware.lavine.service;

import com.cemware.lavine.dto.GroupResponse;
import com.cemware.lavine.dto.UserCreateRequest;
import com.cemware.lavine.dto.UserResponse;
import com.cemware.lavine.dto.UserUpdateRequest;
import com.cemware.lavine.entity.User;
import com.cemware.lavine.exception.ResourceNotFoundException;
import com.cemware.lavine.mapper.TaskMapper;
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

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        User user = new User(request.name());
        User savedUser = userRepository.save(user);
        return new UserResponse(
                savedUser.getId(),
                savedUser.getName()
        );
    }

    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("유저", id));
        user.changeName(request.name());
        return new UserResponse(
                user.getId(),
                user.getName()
        );
    }

    @Transactional
    public void deleteUser(Long id) { // 하위그룹, 과제도 cascade로 자동 삭제
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("유저", id));
        userRepository.delete(user);
    }

    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("유저", id));
        return new UserResponse(
                user.getId(),
                user.getName()
        );
    }

    public List<GroupResponse> getUserGroups(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("유저", userId));
        
        return user.getGroups().stream()
                .map(group -> new GroupResponse(
                        group.getId(),
                        group.getName(),
                        group.getUser().getId(),
                        group.getTasks().stream()
                                .map(TaskMapper::toTaskResponse)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
}

