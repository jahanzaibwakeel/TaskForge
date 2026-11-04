package com.taskforge.taskforge.service.organizationmember;

import com.taskforge.taskforge.entity.OrganizationMember;
import com.taskforge.taskforge.repository.OrganizationMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class OrganizationMemberService {

    private final OrganizationMemberRepository organizationMemberRepository;

    public OrganizationMemberService(
            OrganizationMemberRepository organizationMemberRepository
    ) {
        this.organizationMemberRepository = organizationMemberRepository;
    }

    /**
     * Retrieves a membership and throws an exception if it doesn't exist.
     */
    public OrganizationMember getMembershipOrThrow(
            UUID organizationId,
            UUID userId
    ) {
        return organizationMemberRepository
                .findByOrganizationIdAndUserId(organizationId, userId)
                .orElseThrow(() ->
                        new OrganizationMemberNotFoundException(
                                "Membership not found for user ID: "
                                        + userId
                                        + " in organization ID: "
                                        + organizationId
                        )
                );
    }

    /**
     * Finds a membership without requiring it to exist.
     */
    public Optional<OrganizationMember> findMembership(
            UUID organizationId,
            UUID userId
    ) {
        return organizationMemberRepository
                .findByOrganizationIdAndUserId(organizationId, userId);
    }

    /**
     * Retrieves all members of an organization.
     */
    public List<OrganizationMember> getOrganizationMembers(
            UUID organizationId
    ) {
        return organizationMemberRepository
                .findAllByOrganizationId(organizationId);
    }

    /**
     * Retrieves all organization memberships belonging to a user.
     */
    public List<OrganizationMember> getUserMemberships(UUID userId) {
        return organizationMemberRepository.findAllByUserId(userId);
    }

    /**
     * Checks whether a user belongs to an organization.
     */
    public boolean isOrganizationMember(
            UUID organizationId,
            UUID userId
    ) {
        return organizationMemberRepository
                .existsByOrganizationIdAndUserId(organizationId, userId);
    }
}