package com.taskforge.taskforge.service.projectcollaborationrequest;

import com.taskforge.taskforge.entity.ProjectCollaborationRequest;
import com.taskforge.taskforge.repository.ProjectCollaborationRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProjectCollaborationRequestService {
    private final ProjectCollaborationRequestRepository collaborationRequestRepository;

    public ProjectCollaborationRequestService(
            ProjectCollaborationRequestRepository collaborationRequestRepository
    ) {
        this.collaborationRequestRepository = collaborationRequestRepository;
    }

    /**
     * Retrieves collaboration requests sent to an organization
     * with a particular status.
     */
    public List<ProjectCollaborationRequest> getRequestsForOrganization(
            UUID targetOrganizationId,
            String status
    ) {
        return collaborationRequestRepository
                .findAllByTargetOrganizationIdAndStatus(
                        targetOrganizationId,
                        status
                );
    }

    /**
     * Retrieves all collaboration requests initiated by an organization.
     */
    public List<ProjectCollaborationRequest> getRequestsByOrganization(
            UUID requestingOrganizationId
    ) {
        return collaborationRequestRepository
                .findAllByRequestingOrganizationId(requestingOrganizationId);
    }

    /**
     * Finds a collaboration request between two organizations for a project.
     */
    public Optional<ProjectCollaborationRequest> findCollaborationRequest(
            UUID projectId,
            UUID requestingOrganizationId,
            UUID targetOrganizationId
    ) {
        return collaborationRequestRepository
                .findByProjectIdAndRequestingOrganizationIdAndTargetOrganizationId(
                        projectId,
                        requestingOrganizationId,
                        targetOrganizationId
                );
    }

    /**
     * Retrieves a collaboration request or throws an exception.
     */
    public ProjectCollaborationRequest getCollaborationRequestOrThrow(
            UUID projectId,
            UUID requestingOrganizationId,
            UUID targetOrganizationId
    ) {
        return findCollaborationRequest(
                projectId,
                requestingOrganizationId,
                targetOrganizationId
        ).orElseThrow(() ->
                new ProjectCollaborationRequestNotFoundException(
                        "Collaboration request not found for project ID: "
                                + projectId
                )
        );
    }

    /**
     * Checks whether a collaboration request already exists.
     */
    public boolean collaborationRequestExists(
            UUID projectId,
            UUID requestingOrganizationId,
            UUID targetOrganizationId
    ) {
        return findCollaborationRequest(
                projectId,
                requestingOrganizationId,
                targetOrganizationId
        ).isPresent();
    }
}
