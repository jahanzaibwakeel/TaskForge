package com.taskforge.taskforge.repository;

import com.taskforge.taskforge.entity.EmailDelivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmailDeliveryRepository
        extends JpaRepository<EmailDelivery, UUID> {

    List<EmailDelivery> findAllByStatus(String status);
}