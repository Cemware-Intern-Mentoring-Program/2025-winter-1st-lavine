package com.cemware.lavine.repository;

import com.cemware.lavine.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}