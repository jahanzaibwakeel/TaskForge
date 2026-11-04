package com.taskforge.taskforge.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "organization_id",
            foreignKey = @ForeignKey(
                    name = "fk_audit_logs_organization"
            )
    )
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(
                    name = "fk_audit_logs_user"
            )
    )
    private User user;

    @Column(
            name = "action",
            nullable = false,
            length = 100
    )
    private String action;

    @Column(
            name = "entity_type",
            nullable = false,
            length = 50
    )
    private String entityType;

    @Column(
            name = "entity_id",
            nullable = false
    )
    private UUID entityId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(
            name = "old_values",
            columnDefinition = "jsonb"
    )
    private Map<String, Object> oldValues;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(
            name = "new_values",
            columnDefinition = "jsonb"
    )
    private Map<String, Object> newValues;

    @Column(
            name = "ip_address",
            columnDefinition = "inet"
    )
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(
            name = "created_at",
            nullable = false
    )
    private Instant createdAt;
}