package com.cemware.lavine.controller;

import com.cemware.lavine.dto.TaskResponse;
import com.cemware.lavine.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskController 테스트")
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @Test
    @DisplayName("할 일 조회 API - 성공")
    void getTask_Success() {
        // given
        Long taskId = 1L;
        TaskResponse taskResponse = new TaskResponse(taskId, "테스트 코드 작성", false, 1L, 1L);
        given(taskService.getTask(taskId)).willReturn(taskResponse);

        // when
        ResponseEntity<TaskResponse> response = taskController.getTask(taskId);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(taskId);
        assertThat(response.getBody().title()).isEqualTo("테스트 코드 작성");
        assertThat(response.getBody().done()).isFalse();
    }
}
