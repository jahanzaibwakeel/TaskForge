package com.taskforge.taskforge.service.organization;

import com.taskforge.taskforge.entity.Organization;
import com.taskforge.taskforge.repository.OrganizationRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(
            OrganizationRepository organizationRepository
    ) {
        this.organizationRepository = organizationRepository;
    }

    public Organization getOrganizationById(UUID organizationId) {
        return organizationRepository.findById(organizationId)
                .orElseThrow(() ->
                        new OrganizationNotFoundException(
                                "Organization not found with ID: "
                                        + organizationId
                        )
                );
    }

    public Organization getOrganizationBySlug(String slug) {
        return organizationRepository.findBySlug(slug)
                .orElseThrow(() ->
                        new OrganizationNotFoundException(
                                "Organization not found with slug: " + slug
                        )
                );
    }

    public Optional<Organization> findOrganizationBySlug(String slug) {
        return organizationRepository.findBySlug(slug);
    }

    public boolean slugExists(String slug) {
        return organizationRepository.existsBySlug(slug);
    }
}