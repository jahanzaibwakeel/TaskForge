package com.taskforge.taskforge.repository;

import com.taskforge.taskforge.entity.ProjectMember;
import com.taskforge.taskforge.entity.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectMemberRepository
        extends JpaRepository<ProjectMember, ProjectMemberId> {

    List<ProjectMember> findAllByProjectId(UUID projectId);

    List<ProjectMember> findAllByUserId(UUID userId);

    List<ProjectMember> findAllByProjectIdAndOrganizationId(
            UUID projectId,
            UUID organizationId
    );

    Optional<ProjectMember> findByProjectIdAndUserId(
            UUID projectId,
            UUID userId
    );

    boolean existsByProjectIdAndUserId(
            UUID projectId,
            UUID userId
    );
}