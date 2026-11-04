package com.taskforge.taskforge.repository;

import com.taskforge.taskforge.entity.OrganizationMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationMemberRepository
        extends JpaRepository<OrganizationMember, UUID> {

    Optional<OrganizationMember> findByOrganizationIdAndUserId(
            UUID organizationId,
            UUID userId
    );

    List<OrganizationMember> findAllByOrganizationId(
            UUID organizationId
    );

    List<OrganizationMember> findAllByUserId(
            UUID userId
    );

    boolean existsByOrganizationIdAndUserId(
            UUID organizationId,
            UUID userId
    );
}