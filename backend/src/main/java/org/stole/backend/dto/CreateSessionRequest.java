package org.stole.backend.dto;

import org.stole.backend.model.MuscleGroup;

import java.time.LocalDateTime;
import java.util.Set;

public class CreateSessionRequest {
    public Set<MuscleGroup> muscleGroups;
    public LocalDateTime dateTime;
    public String notes;
    public Long userId;
}
