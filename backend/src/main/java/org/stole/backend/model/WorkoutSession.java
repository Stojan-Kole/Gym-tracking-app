package org.stole.backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Entity
public class WorkoutSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String name;
    private LocalDateTime dateTime;

    @ElementCollection(targetClass = MuscleGroup.class)
    @CollectionTable(name = "session_muscle_groups", joinColumns = @JoinColumn(name = "session_id"))
    @Enumerated(EnumType.STRING)
    private Set<MuscleGroup> muscleGroups;

    private String notes;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Exercise> exercises;

    private Long userId;

    public WorkoutSession() {
        this.exercises = new ArrayList<>();
    }

    public WorkoutSession(String name, LocalDateTime dateTime, String notes) {
        this.name = name;
        this.dateTime = dateTime;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public LocalDateTime getDateTime() { return dateTime; }

    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getNotes() { return notes; }

    public void setNotes(String notes) { this.notes = notes; }

    public List<Exercise> getExercises() { return exercises; }

    public void setExercises(List<Exercise> exercises) { this.exercises = exercises; }

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
        exercise.setSession(this);
    }

    public void removeExercise(Exercise exercise) {
        exercises.remove(exercise);
        exercise.setSession(null);
    }

    public void setMuscleGroups(Set<MuscleGroup> muscleGroups) {
        this.muscleGroups = muscleGroups;
    }

    public Set<MuscleGroup> getMuscleGroups() {
        return muscleGroups;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
