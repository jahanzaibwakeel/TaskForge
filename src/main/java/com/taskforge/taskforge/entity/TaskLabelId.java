package com.taskforge.taskforge.entity;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TaskLabelId implements Serializable {

    private UUID taskId;
    private UUID labelId;
}