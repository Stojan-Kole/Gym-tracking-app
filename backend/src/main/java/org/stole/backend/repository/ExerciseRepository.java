package org.stole.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stole.backend.model.Exercise;
import org.stole.backend.model.MuscleGroup;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByMuscleGroupsIn(List<MuscleGroup> groups);
}
