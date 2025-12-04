package com.cemware.lavine.repository;

import com.cemware.lavine.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}