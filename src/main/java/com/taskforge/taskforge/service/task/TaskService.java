package com.taskforge.taskforge.service.task;

import com.taskforge.taskforge.entity.Task;
import com.taskforge.taskforge.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Retrieves a task by its ID.
     */
    public Task getTaskById(UUID taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task not found with ID: " + taskId
                        )
                );
    }

    /**
     * Retrieves all tasks belonging to a project.
     */
    public List<Task> getProjectTasks(UUID projectId) {
        return taskRepository.findAllByProjectId(projectId);
    }

    /**
     * Retrieves all tasks belonging to a project with a particular status.
     */
    public List<Task> getProjectTasksByStatus(
            UUID projectId,
            String status
    ) {
        return taskRepository.findAllByProjectIdAndStatus(
                projectId,
                status
        );
    }

    /**
     * Retrieves all tasks assigned to a user.
     */
    public List<Task> getAssignedTasks(UUID userId) {
        return taskRepository.findAllByAssignedToId(userId);
    }

    /**
     * Retrieves all tasks created by a user.
     */
    public List<Task> getCreatedTasks(UUID userId) {
        return taskRepository.findAllByCreatedById(userId);
    }

    /**
     * Checks whether a task exists.
     */
    public boolean taskExists(UUID taskId) {
        return taskRepository.existsById(taskId);
    }
}
