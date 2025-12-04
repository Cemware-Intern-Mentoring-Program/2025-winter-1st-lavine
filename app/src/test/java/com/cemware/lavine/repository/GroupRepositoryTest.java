package com.cemware.lavine.repository;

import com.cemware.lavine.entity.Group;
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
@DisplayName("GroupRepository 테스트")
class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("그룹 ID로 조회 - 성공")
    void findById_Success() {
        // given
        User user = new User("홍길동");
        userRepository.save(user);

        Group group = new Group(user, "개인 프로젝트");
        Group savedGroup = groupRepository.save(group);

        // when
        Optional<Group> found = groupRepository.findById(savedGroup.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(savedGroup.getId());
        assertThat(found.get().getName()).isEqualTo("개인 프로젝트");
        assertThat(found.get().getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("그룹 ID로 조회 - 존재하지 않는 경우")
    void findById_NotFound() {
        // given
        Long nonExistentId = 999L;

        // when
        Optional<Group> found = groupRepository.findById(nonExistentId);

        // then
        assertThat(found).isEmpty();
    }
}
