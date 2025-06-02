package org.stole.backend.controller;

import org.springframework.web.bind.annotation.*;

import org.stole.backend.dto.WorkoutSessionRequest;
import org.stole.backend.dto.ExerciseRequest;
import org.stole.backend.dto.SetEntryRequest;

import org.stole.backend.model.*;
import org.stole.backend.repository.WorkoutSessionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/sessions")
public class WorkoutSessionController {

    private final WorkoutSessionRepository sessionRepository;

    public WorkoutSessionController(WorkoutSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @GetMapping
    public List<WorkoutSession> getAllSessions() {
        System.out.println("Get all sessions");
        return sessionRepository.findAll();
    }

    @PostMapping
    public WorkoutSession createSession(@RequestBody WorkoutSessionRequest request) {
        WorkoutSession session = new WorkoutSession();

        // Convert muscle groups from String to enum
        Set<MuscleGroup> muscleGroups = request.getMuscleGroups().stream()
                .map(MuscleGroup::valueOf)
                .collect(Collectors.toSet());

        session.setMuscleGroups(muscleGroups);
        session.setDateTime(request.getDateTime());
        session.setNotes(request.getNotes());
        session.setUserId(request.getUserId());

        String name = muscleGroups.stream()
                .map(Enum::name)
                .collect(Collectors.joining("_"));
        session.setName(name);

        session.setExercises(new ArrayList<>());

        // Map exercises with nested sets
        if (request.getExercises() != null) {
            for (ExerciseRequest exReq : request.getExercises()) {
                Exercise exercise = new Exercise();
                exercise.setName(exReq.getName());

                // Convert muscle groups of exercise
                if (exReq.getMuscleGroups() != null) {
                    Set<MuscleGroup> exerciseMuscleGroups = exReq.getMuscleGroups().stream()
                            .map(MuscleGroup::valueOf)
                            .collect(Collectors.toSet());
                    exercise.setMuscleGroups(exerciseMuscleGroups);
                }

                exercise.setSession(session);

                // Convert sets to SetEntry list
                List<SetEntry> sets = new ArrayList<>();
                if (exReq.getSets() != null) {
                    for (SetEntryRequest setReq : exReq.getSets()) {
                        SetEntry setEntry = new SetEntry();
                        setEntry.setReps(setReq.getReps());
                        setEntry.setWeight(setReq.getWeight());
                        setEntry.setExercise(exercise);
                        sets.add(setEntry);
                    }
                }
                exercise.setSets(sets);

                session.getExercises().add(exercise);
            }
        }

        return sessionRepository.save(session);
    }

    @DeleteMapping("/{id}")
    public void deleteSession(@PathVariable Long id) {
        System.out.println("Delete session: " + id);
        sessionRepository.deleteById(id);
    }
}
