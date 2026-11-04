package com.taskforge.taskforge.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "project_collaboration_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectCollaborationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "project_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_collab_requests_project"
            )
    )
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "requesting_organization_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_collab_requests_requesting_org"
            )
    )
    private Organization requestingOrganization;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "target_organization_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_collab_requests_target_org"
            )
    )
    private Organization targetOrganization;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "requested_by",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_collab_requests_requested_by"
            )
    )
    private User requestedBy;

    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "responded_by",
            foreignKey = @ForeignKey(
                    name = "fk_collab_requests_responded_by"
            )
    )
    private User respondedBy;

    @Column(name = "responded_at")
    private Instant respondedAt;

    @Column(name = "message")
    private String message;

    @Column(
            name = "created_at",
            nullable = false
    )
    private Instant createdAt;
}