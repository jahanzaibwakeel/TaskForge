package com.taskforge.taskforge.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "email_deliveries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(
                    name = "fk_email_deliveries_user"
            )
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "notification_id",
            foreignKey = @ForeignKey(
                    name = "fk_email_deliveries_notification"
            )
    )
    private Notification notification;

    @Column(
            name = "recipient_email",
            nullable = false,
            columnDefinition = "citext"
    )
    private String recipientEmail;

    @Column(
            name = "email_type",
            nullable = false,
            length = 50
    )
    private String emailType;

    @Column(
            name = "subject",
            nullable = false,
            length = 255
    )
    private String subject;

    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    private String status;

    @Column(
            name = "attempts",
            nullable = false
    )
    private Integer attempts;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "last_error")
    private String lastError;

    @Column(
            name = "created_at",
            nullable = false
    )
    private Instant createdAt;
}