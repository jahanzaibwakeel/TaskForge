package com.taskforge.taskforge.repository;

import com.taskforge.taskforge.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {

    List<Attachment> findAllByTaskId(UUID taskId);
}