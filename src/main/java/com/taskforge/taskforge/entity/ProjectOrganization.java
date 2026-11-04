package com.taskforge.taskforge.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "project_organizations")
@IdClass(ProjectOrganizationId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectOrganization {

    @Id
    @Column(name = "project_id")
    private UUID projectId;

    @Id
    @Column(name = "organization_id")
    private UUID organizationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "project_id",
            nullable = false,
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(
                    name = "fk_project_orgs_project"
            )
    )
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "organization_id",
            nullable = false,
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(
                    name = "fk_project_orgs_organization"
            )
    )
    private Organization organization;

    @Column(
            name = "role",
            nullable = false,
            length = 20
    )
    private String role;

    @Column(
            name = "joined_at",
            nullable = false
    )
    private Instant joinedAt;
}