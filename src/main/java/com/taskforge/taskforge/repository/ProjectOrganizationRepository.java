package com.taskforge.taskforge.repository;

import com.taskforge.taskforge.entity.ProjectOrganization;
import com.taskforge.taskforge.entity.ProjectOrganizationId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectOrganizationRepository
        extends JpaRepository<ProjectOrganization, ProjectOrganizationId> {

    List<ProjectOrganization> findAllByProjectId(UUID projectId);

    List<ProjectOrganization> findAllByOrganizationId(UUID organizationId);

    Optional<ProjectOrganization> findByProjectIdAndOrganizationId(
            UUID projectId,
            UUID organizationId
    );

    boolean existsByProjectIdAndOrganizationId(
            UUID projectId,
            UUID organizationId
    );
}