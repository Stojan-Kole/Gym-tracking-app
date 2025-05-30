package org.stole.backend.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String setsJson;

    @ElementCollection(targetClass = MuscleGroup.class)
    @CollectionTable(name = "exercise_muscle_groups", joinColumns = @JoinColumn(name = "exercise_id"))
    @Enumerated(EnumType.STRING)
    private Set<MuscleGroup> muscleGroups;


    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private WorkoutSession session;

    // Constructors
    public Exercise() {}

    public Exercise(String name, String setsJson, Set<MuscleGroup> muscleGroups) {
        this.name = name;
        this.setsJson = setsJson;
        this.muscleGroups = muscleGroups;
    }

    // Getters and Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getSetsJson() { return setsJson; }

    public void setSetsJson(String setsJson) { this.setsJson = setsJson; }

    public WorkoutSession getSession() { return session; }

    public void setSession(WorkoutSession session) { this.session = session; }

    public Set<MuscleGroup> getMuscleGroups() {
        return muscleGroups;
    }
}