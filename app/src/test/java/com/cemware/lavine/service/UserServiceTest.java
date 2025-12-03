package com.cemware.lavine.service;

import com.cemware.lavine.dto.GroupResponse;
import com.cemware.lavine.dto.UserResponse;
import com.cemware.lavine.entity.Group;
import com.cemware.lavine.entity.Task;
import com.cemware.lavine.entity.User;
import com.cemware.lavine.exception.ResourceNotFoundException;
import com.cemware.lavine.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 테스트")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("유저 조회 - 성공")
    void getUser_Success() {
        // given
        Long userId = 1L;
        User user = new User("홍길동");
        user.getClass(); 

        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));

        // when
        UserResponse response = userService.getUser(userId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("유저 조회 - 존재하지 않는 경우")
    void getUser_NotFound() {
        // given
        Long userId = 999L;
        given(userRepository.findById(userId))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getUser(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("찾을 수 없습니다");
    }

    @Test
    @DisplayName("유저의 그룹 목록 조회 - 성공")
    void getUserGroups_Success() {
        // given
        Long userId = 1L;
        User user = new User("홍길동");
        
        Group group1 = new Group(user, "프로젝트");
        Group group2 = new Group(user, "개인");

        Task task1 = Task.builder()
                .title("할일1")
                .group(group1)
                .user(user)
                .build();

        group1.getTasks().add(task1);

        user.getGroups().add(group1);
        user.getGroups().add(group2);

        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));

        // when
        List<GroupResponse> responses = userService.getUserGroups(userId);

        // then
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).name()).isEqualTo("프로젝트");
        assertThat(responses.get(0).tasks()).hasSize(1);
        assertThat(responses.get(1).name()).isEqualTo("개인");
    }

    @Test
    @DisplayName("유저의 그룹 목록 조회 - 유저가 존재하지 않는 경우")
    void getUserGroups_UserNotFound() {
        // given
        Long userId = 999L;
        given(userRepository.findById(userId))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getUserGroups(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("찾을 수 없습니다");
    }
}
