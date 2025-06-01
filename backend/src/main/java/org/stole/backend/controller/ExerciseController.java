package org.stole.backend.controller;


import org.hibernate.jdbc.Expectation;
import org.springframework.web.bind.annotation.*;
import org.stole.backend.model.Exercise;
import org.stole.backend.model.MuscleGroup;

import javax.lang.model.type.NullType;
import java.util.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {
    private static final Map<MuscleGroup, List<String>> EXERCISES_BY_MUSCLE_GROUP = new HashMap<>();

    static {
        EXERCISES_BY_MUSCLE_GROUP.put(MuscleGroup.LEGS, Arrays.asList("Squats", "Leg Press", "Lunges"));
        EXERCISES_BY_MUSCLE_GROUP.put(MuscleGroup.TRICEPS, Arrays.asList("Tricep Dips", "Tricep Pushdown"));
        EXERCISES_BY_MUSCLE_GROUP.put(MuscleGroup.BICEPS, Arrays.asList("Barbell Curl", "Hammer Curl"));
        EXERCISES_BY_MUSCLE_GROUP.put(MuscleGroup.SHOULDERS, Arrays.asList("Shoulder Press", "Lateral Raises"));
        EXERCISES_BY_MUSCLE_GROUP.put(MuscleGroup.CHEST, Arrays.asList("Bench Press", "Incline Bench Press", "Push-ups"));
        EXERCISES_BY_MUSCLE_GROUP.put(MuscleGroup.BACK, Arrays.asList("Pull-ups", "Deadlift", "Rows"));
    }

    @GetMapping
    public List<String> getExercisesByMuscleGroups(@RequestParam List<MuscleGroup> muscleGroups) {
        List<String> result = new ArrayList<>();
        for (MuscleGroup group : muscleGroups) {
            List<String> exercises = EXERCISES_BY_MUSCLE_GROUP.get(group);
            if (exercises != null) {
                result.addAll(exercises);
            }
        }
        return result;
    }
}
