package com.cemware.lavine.repository;

import com.cemware.lavine.entity.Group;
import com.cemware.lavine.entity.Task;
import com.cemware.lavine.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("TaskRepository 테스트")
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Test
    @DisplayName("할 일 ID로 조회 - 성공")
    void findById_Success() {
        // given
        User user = new User("홍길동");
        userRepository.save(user);

        Group group = new Group(user, "프로젝트");
        groupRepository.save(group);

        Task task = Task.builder()
                .title("테스트 작성하기")
                .group(group)
                .user(user)
                .build();
        Task savedTask = taskRepository.save(task);

        // when
        Optional<Task> found = taskRepository.findById(savedTask.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(savedTask.getId());
        assertThat(found.get().getTitle()).isEqualTo("테스트 작성하기");
        assertThat(found.get().isDone()).isFalse();
        assertThat(found.get().getGroup().getId()).isEqualTo(group.getId());
        assertThat(found.get().getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("할 일 ID로 조회 - 존재하지 않는 경우")
    void findById_NotFound() {
        // given
        Long nonExistentId = 999L;

        // when
        Optional<Task> found = taskRepository.findById(nonExistentId);

        // then
        assertThat(found).isEmpty();
    }
}
