package com.taskforge.taskforge.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "organization_members",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_org_members_user",
                        columnNames = {"organization_id", "user_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationMember {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "organization_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_org_members_organization"
            )
    )
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_org_members_user"
            )
    )
    private User user;

    @Column(
            name = "platform_role",
            nullable = false,
            length = 20
    )
    private String platformRole;

    @Column(
            name = "job_title",
            length = 100
    )
    private String jobTitle;

    @Column(
            name = "department",
            length = 100
    )
    private String department;

    @Column(
            name = "joined_at",
            nullable = false
    )
    private Instant joinedAt;
}