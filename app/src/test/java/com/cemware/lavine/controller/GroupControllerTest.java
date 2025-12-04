package com.cemware.lavine.controller;

import com.cemware.lavine.dto.GroupResponse;
import com.cemware.lavine.dto.TaskResponse;
import com.cemware.lavine.service.GroupService;
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
@DisplayName("GroupController 테스트")
class GroupControllerTest {

    @Mock
    private GroupService groupService;

    @InjectMocks
    private GroupController groupController;

    @Test
    @DisplayName("그룹 조회 API - 성공")
    void getGroup_Success() {
        // given
        Long groupId = 1L;
        Long userId = 1L;
        
        TaskResponse taskResponse = new TaskResponse(1L, "할일1", false, groupId, userId);
        GroupResponse groupResponse = new GroupResponse(groupId, "프로젝트", userId, List.of(taskResponse));
        
        given(groupService.getGroup(groupId)).willReturn(groupResponse);

        // when
        ResponseEntity<GroupResponse> response = groupController.getGroup(groupId);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(groupId);
        assertThat(response.getBody().name()).isEqualTo("프로젝트");
        assertThat(response.getBody().tasks()).hasSize(1);
        assertThat(response.getBody().tasks().get(0).title()).isEqualTo("할일1");
    }
}
