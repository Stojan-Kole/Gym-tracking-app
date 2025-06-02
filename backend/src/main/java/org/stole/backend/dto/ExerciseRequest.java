package org.stole.backend.dto;

import java.util.List;
import java.util.Set;

public class ExerciseRequest {
    private String name;
    private List<SetEntryRequest> sets;
    private Set<String> muscleGroups;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<SetEntryRequest> getSets() { return sets; }
    public void setSets(List<SetEntryRequest> sets) { this.sets = sets; }

    public Set<String> getMuscleGroups() { return muscleGroups; }
    public void setMuscleGroups(Set<String> muscleGroups) { this.muscleGroups = muscleGroups; }
}
