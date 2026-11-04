package com.taskforge.taskforge.repository;

import com.taskforge.taskforge.entity.ProjectCollaborationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectCollaborationRequestRepository
        extends JpaRepository<ProjectCollaborationRequest, UUID> {

    List<ProjectCollaborationRequest> findAllByTargetOrganizationIdAndStatus(
            UUID targetOrganizationId,
            String status
    );

    List<ProjectCollaborationRequest> findAllByRequestingOrganizationId(
            UUID requestingOrganizationId
    );

    Optional<ProjectCollaborationRequest> findByProjectIdAndRequestingOrganizationIdAndTargetOrganizationId(
            UUID projectId,
            UUID requestingOrganizationId,
            UUID targetOrganizationId
    );
}