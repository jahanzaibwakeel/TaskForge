package com.taskforge.taskforge.service.projectmember;

import com.taskforge.taskforge.entity.ProjectMember;
import com.taskforge.taskforge.repository.ProjectMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;

    public ProjectMemberService(ProjectMemberRepository projectMemberRepository) {
        this.projectMemberRepository = projectMemberRepository;
    }

    /**
     * Retrieves all members assigned to a project.
     */
    public List<ProjectMember> getProjectMembers(UUID projectId) {
        return projectMemberRepository.findAllByProjectId(projectId);
    }

    /**
     * Retrieves all project memberships belonging to a user.
     */
    public List<ProjectMember> getUserProjectMemberships(UUID userId) {
        return projectMemberRepository.findAllByUserId(userId);
    }

    /**
     * Retrieves project members belonging to a particular organization.
     */
    public List<ProjectMember> getProjectMembersByOrganization(
            UUID projectId,
            UUID organizationId
    ) {
        return projectMemberRepository.findAllByProjectIdAndOrganizationId(
                projectId,
                organizationId
        );
    }

    /**
     * Finds a user's membership in a particular project.
     */
    public Optional<ProjectMember> findProjectMembership(
            UUID projectId,
            UUID userId
    ) {
        return projectMemberRepository.findByProjectIdAndUserId(
                projectId,
                userId
        );
    }

    /**
     * Retrieves a user's project membership or throws an exception.
     */
    public ProjectMember getProjectMembershipOrThrow(
            UUID projectId,
            UUID userId
    ) {
        return projectMemberRepository
                .findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() ->
                        new ProjectMemberNotFoundException(
                                "Project membership not found for user ID: "
                                        + userId + " in project ID: " + projectId
                        )
                );
    }

    /**
     * Checks whether a user belongs to a particular project.
     */
    public boolean isProjectMember(UUID projectId, UUID userId) {
        return projectMemberRepository.existsByProjectIdAndUserId(
                projectId,
                userId
        );
    }
}
