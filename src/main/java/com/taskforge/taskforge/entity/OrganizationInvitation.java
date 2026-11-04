package com.taskforge.taskforge.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "organization_invitations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_org_invites_token",
                        columnNames = "token_hash"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "organization_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_org_invites_organization"
            )
    )
    private Organization organization;

    @Column(
            name = "email",
            nullable = false,
            columnDefinition = "citext"
    )
    private String email;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "invited_by",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_org_invites_invited_by"
            )
    )
    private User invitedBy;

    @Column(
            name = "platform_role",
            nullable = false,
            length = 20
    )
    private String platformRole;

    @Column(name = "job_title", length = 100)
    private String jobTitle;

    @Column(name = "department", length = 100)
    private String department;

    @Column(
            name = "token_hash",
            nullable = false,
            unique = true,
            length = 255
    )
    private String tokenHash;

    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    private String status;

    @Column(
            name = "expires_at",
            nullable = false
    )
    private Instant expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "accepted_by",
            foreignKey = @ForeignKey(
                    name = "fk_org_invites_accepted_by"
            )
    )
    private User acceptedBy;

    @Column(name = "accepted_at")
    private Instant acceptedAt;

    @Column(
            name = "created_at",
            nullable = false
    )
    private Instant createdAt;
}