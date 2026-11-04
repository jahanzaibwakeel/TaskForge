package com.taskforge.taskforge.repository;

import com.taskforge.taskforge.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository
        extends JpaRepository<Notification, UUID> {

    List<Notification> findAllByUserIdOrderByCreatedAtDesc(
            UUID userId
    );

    List<Notification> findAllByUserIdAndIsReadOrderByCreatedAtDesc(
            UUID userId,
            boolean isRead
    );
}