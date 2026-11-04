package com.taskforge.taskforge.entity;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProjectOrganizationId implements Serializable {

    private UUID projectId;
    private UUID organizationId;
}