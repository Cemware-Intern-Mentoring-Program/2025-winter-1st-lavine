package com.cemware.lavine.repository;

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
@DisplayName("UserRepository 테스트")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저 ID로 조회 - 성공")
    void findById_Success() {
        // given
        User user = new User("홍길동");
        User savedUser = userRepository.save(user);

        // when
        Optional<User> found = userRepository.findById(savedUser.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(savedUser.getId());
        assertThat(found.get().getName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("유저 ID로 조회 - 존재하지 않는 경우")
    void findById_NotFound() {
        // given
        Long nonExistentId = 999L;

        // when
        Optional<User> found = userRepository.findById(nonExistentId);

        // then
        assertThat(found).isEmpty();
    }
}
