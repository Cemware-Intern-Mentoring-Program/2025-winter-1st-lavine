package com.cemware.lavine.service;

import com.cemware.lavine.dto.TaskResponse;
import com.cemware.lavine.entity.Group;
import com.cemware.lavine.entity.Task;
import com.cemware.lavine.entity.User;
import com.cemware.lavine.exception.ResourceNotFoundException;
import com.cemware.lavine.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskService 테스트")
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    @DisplayName("할 일 조회 - 성공")
    void getTask_Success() {
        // given
        Long taskId = 1L;
        User user = new User("홍길동");
        Group group = new Group(user, "프로젝트");
        
        Task task = Task.builder()
                .title("테스트 코드 작성")
                .group(group)
                .user(user)
                .build();

        given(taskRepository.findById(taskId))
                .willReturn(Optional.of(task));

        // when
        TaskResponse response = taskService.getTask(taskId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("테스트 코드 작성");
        assertThat(response.done()).isFalse();
    }

    @Test
    @DisplayName("할 일 조회 - 존재하지 않는 경우")
    void getTask_NotFound() {
        // given
        Long taskId = 999L;
        given(taskRepository.findById(taskId))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> taskService.getTask(taskId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("찾을 수 없습니다");
    }
}
