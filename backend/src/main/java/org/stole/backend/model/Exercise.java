package org.stole.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Entity
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection(targetClass = MuscleGroup.class)
    @CollectionTable(name = "exercise_muscle_groups", joinColumns = @JoinColumn(name = "exercise_id"))
    @Enumerated(EnumType.STRING)
    private Set<MuscleGroup> muscleGroups;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    @JsonBackReference
    private WorkoutSession session;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SetEntry> sets = new ArrayList<>();

    // Constructors
    public Exercise() {}

    public Exercise(String name, Set<MuscleGroup> muscleGroups) {
        this.name = name;
        this.muscleGroups = muscleGroups;
    }

    // Getters and Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Set<MuscleGroup> getMuscleGroups() { return muscleGroups; }

    public void setMuscleGroups(Set<MuscleGroup> muscleGroups) { this.muscleGroups = muscleGroups; }

    public WorkoutSession getSession() { return session; }

    public void setSession(WorkoutSession session) { this.session = session; }

    public List<SetEntry> getSets() { return sets; }

    public void setSets(List<SetEntry> sets) { this.sets = sets; }
}
