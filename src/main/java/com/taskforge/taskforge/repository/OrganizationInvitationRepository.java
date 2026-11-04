package com.taskforge.taskforge.repository;

import com.taskforge.taskforge.entity.OrganizationInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationInvitationRepository
        extends JpaRepository<OrganizationInvitation, UUID> {

    List<OrganizationInvitation> findAllByOrganizationIdAndStatus(
            UUID organizationId,
            String status
    );

    List<OrganizationInvitation> findAllByEmailAndStatus(
            String email,
            String status
    );

    Optional<OrganizationInvitation> findByTokenHash(String tokenHash);
}