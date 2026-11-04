package com.taskforge.taskforge.repository;

import com.taskforge.taskforge.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LabelRepository extends JpaRepository<Label, UUID> {

    List<Label> findAllByOrganizationId(UUID organizationId);
}