package org.stole.backend.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class WorkoutSessionRequest {
    private LocalDateTime dateTime;
    private List<ExerciseRequest> exercises;
    private Set<String> muscleGroups;
    private String name;
    private String notes;
    private Long userId;

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public List<ExerciseRequest> getExercises() { return exercises; }
    public void setExercises(List<ExerciseRequest> exercises) { this.exercises = exercises; }

    public Set<String> getMuscleGroups() { return muscleGroups; }
    public void setMuscleGroups(Set<String> muscleGroups) { this.muscleGroups = muscleGroups; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
