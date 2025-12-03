package com.cemware.lavine.service;

import com.cemware.lavine.dto.GroupResponse;
import com.cemware.lavine.entity.Group;
import com.cemware.lavine.entity.Task;
import com.cemware.lavine.entity.User;
import com.cemware.lavine.exception.ResourceNotFoundException;
import com.cemware.lavine.repository.GroupRepository;
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
@DisplayName("GroupService 테스트")
class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private GroupService groupService;

    @Test
    @DisplayName("그룹 조회 - 성공")
    void getGroup_Success() {
        // given
        Long groupId = 1L;
        User user = new User("홍길동");
        Group group = new Group(user, "프로젝트");

        Task task = Task.builder()
                .title("테스트 작성")
                .group(group)
                .user(user)
                .build();
        group.getTasks().add(task);

        given(groupRepository.findById(groupId))
                .willReturn(Optional.of(group));

        // when
        GroupResponse response = groupService.getGroup(groupId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("프로젝트");
        assertThat(response.tasks()).hasSize(1);
        assertThat(response.tasks().get(0).title()).isEqualTo("테스트 작성");
    }

    @Test
    @DisplayName("그룹 조회 - 존재하지 않는 경우")
    void getGroup_NotFound() {
        // given
        Long groupId = 999L;
        given(groupRepository.findById(groupId))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> groupService.getGroup(groupId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("찾을 수 없습니다");
    }
}
