package com.taskforge.taskforge.repository;

import com.taskforge.taskforge.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findAllByProjectId(UUID projectId);

    List<Task> findAllByProjectIdAndStatus(
            UUID projectId,
            String status
    );

    List<Task> findAllByAssignedToId(UUID userId);

    List<Task> findAllByCreatedById(UUID userId);
}