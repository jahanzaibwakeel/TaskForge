package com.taskforge.taskforge.service.projectorganization;

import com.taskforge.taskforge.entity.ProjectOrganization;
import com.taskforge.taskforge.repository.ProjectOrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProjectOrganizationService {

    private final ProjectOrganizationRepository projectOrganizationRepository;

    public ProjectOrganizationService(
            ProjectOrganizationRepository projectOrganizationRepository
    ) {
        this.projectOrganizationRepository = projectOrganizationRepository;
    }

    /**
     * Retrieves all organizations participating in a project.
     */
    public List<ProjectOrganization> getProjectOrganizations(
            UUID projectId
    ) {
        return projectOrganizationRepository.findAllByProjectId(projectId);
    }

    /**
     * Retrieves all projects associated with an organization.
     */
    public List<ProjectOrganization> getOrganizationProjects(
            UUID organizationId
    ) {
        return projectOrganizationRepository
                .findAllByOrganizationId(organizationId);
    }

    /**
     * Finds the relationship between a project and an organization.
     */
    public Optional<ProjectOrganization> findRelationship(
            UUID projectId,
            UUID organizationId
    ) {
        return projectOrganizationRepository
                .findByProjectIdAndOrganizationId(
                        projectId,
                        organizationId
                );
    }

    /**
     * Checks whether an organization participates in a project.
     */
    public boolean isOrganizationInProject(
            UUID projectId,
            UUID organizationId
    ) {
        return projectOrganizationRepository
                .existsByProjectIdAndOrganizationId(
                        projectId,
                        organizationId
                );
    }
}