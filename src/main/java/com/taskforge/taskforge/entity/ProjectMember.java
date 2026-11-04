package com.taskforge.taskforge.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "project_members")
@IdClass(ProjectMemberId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectMember {

    @Id
    @Column(name = "project_id")
    private UUID projectId;

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(
            name = "organization_id",
            nullable = false
    )
    private UUID organizationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "project_id",
            nullable = false,
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(
                    name = "fk_project_members_project_org"
            )
    )
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(
                    name = "fk_project_members_org_user"
            )
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns(
            value = {
                    @JoinColumn(
                            name = "project_id",
                            referencedColumnName = "project_id",
                            insertable = false,
                            updatable = false
                    ),
                    @JoinColumn(
                            name = "organization_id",
                            referencedColumnName = "organization_id",
                            insertable = false,
                            updatable = false
                    )
            }
    )
    private ProjectOrganization projectOrganization;

    @Column(
            name = "role",
            nullable = false,
            length = 30
    )
    private String role;

    @Column(
            name = "joined_at",
            nullable = false
    )
    private Instant joinedAt;
}