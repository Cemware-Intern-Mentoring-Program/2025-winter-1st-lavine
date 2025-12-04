package com.cemware.lavine.controller;

import com.cemware.lavine.dto.GroupResponse;
import com.cemware.lavine.dto.TaskResponse;
import com.cemware.lavine.dto.UserResponse;
import com.cemware.lavine.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController 테스트")
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    @DisplayName("유저 조회 API - 성공")
    void getUser_Success() {
        // given
        Long userId = 1L;
        UserResponse userResponse = new UserResponse(userId, "홍길동");
        given(userService.getUser(userId)).willReturn(userResponse);

        // when
        ResponseEntity<UserResponse> response = userController.getUser(userId);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(userId);
        assertThat(response.getBody().name()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("유저가 생성한 그룹 목록 조회 API - 성공")
    void getUserGroups_Success() {
        // given
        Long userId = 1L;
        
        TaskResponse taskResponse = new TaskResponse(1L, "할일1", false, 1L, userId);
        GroupResponse group1 = new GroupResponse(1L, "프로젝트", userId, List.of(taskResponse));
        GroupResponse group2 = new GroupResponse(2L, "개인", userId, List.of());

        given(userService.getUserGroups(userId)).willReturn(List.of(group1, group2));

        // when
        ResponseEntity<List<GroupResponse>> response = userController.getUserGroups(userId);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody().get(0).name()).isEqualTo("프로젝트");
        assertThat(response.getBody().get(0).tasks()).hasSize(1);
        assertThat(response.getBody().get(1).name()).isEqualTo("개인");
    }
}
