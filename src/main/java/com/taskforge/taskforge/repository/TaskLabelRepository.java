package com.taskforge.taskforge.repository;

import com.taskforge.taskforge.entity.TaskLabel;
import com.taskforge.taskforge.entity.TaskLabelId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskLabelRepository
        extends JpaRepository<TaskLabel, TaskLabelId> {

    List<TaskLabel> findAllByTaskId(UUID taskId);

    List<TaskLabel> findAllByLabelId(UUID labelId);

    boolean existsByTaskIdAndLabelId(UUID taskId, UUID labelId);
}