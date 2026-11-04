package com.taskforge.taskforge.service.project;

import com.taskforge.taskforge.entity.Project;
import com.taskforge.taskforge.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * Retrieves a project by its ID.
     */
    public Project getProjectById(UUID projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ProjectNotFoundException(
                                "Project not found with ID: " + projectId
                        )
                );
    }

    /**
     * Retrieves a project by its slug.
     */
    public Project getProjectBySlug(String slug) {
        return projectRepository.findBySlug(slug)
                .orElseThrow(() ->
                        new ProjectNotFoundException(
                                "Project not found with slug: " + slug
                        )
                );
    }

    /**
     * Searches for a project by slug without throwing an exception.
     */
    public Optional<Project> findProjectBySlug(String slug) {
        return projectRepository.findBySlug(slug);
    }

    /**
     * Checks whether a project slug already exists.
     */
    public boolean slugExists(String slug) {
        return projectRepository.existsBySlug(slug);
    }
}