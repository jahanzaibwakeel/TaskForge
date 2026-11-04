package com.taskforge.taskforge.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "labels",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_labels_org_name",
                        columnNames = {"organization_id", "name"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "organization_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_labels_organization"
            )
    )
    private Organization organization;

    @Column(
            name = "name",
            nullable = false,
            length = 100
    )
    private String name;

    @Column(name = "color", length = 20)
    private String color;

    @Column(
            name = "created_at",
            nullable = false
    )
    private Instant createdAt;
}