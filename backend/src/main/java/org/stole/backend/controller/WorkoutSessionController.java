package org.stole.backend.controller;

import org.springframework.web.bind.annotation.*;

import org.stole.backend.model.Exercise;
import org.stole.backend.model.WorkoutSession;
import org.stole.backend.repository.ExerciseRepository;
import org.stole.backend.repository.WorkoutSessionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/sessions")
public class WorkoutSessionController {

    private final WorkoutSessionRepository sessionRepository;
    private final ExerciseRepository exerciseRepository;

    public WorkoutSessionController(WorkoutSessionRepository sessionRepository, ExerciseRepository exerciseRepository) {
        this.sessionRepository = sessionRepository;
        this.exerciseRepository = exerciseRepository;
    }

    @GetMapping
    public List<WorkoutSession> getAllSessions() {
        System.out.println("Get all sessions");
        return sessionRepository.findAll();

    }

    @PostMapping
    public WorkoutSession createSession(@RequestBody WorkoutSession request) {
        WorkoutSession session = new WorkoutSession();

        session.setMuscleGroups(request.getMuscleGroups());
        session.setDateTime(request.getDateTime());
        session.setNotes(request.getNotes());
        session.setUserId(request.getUserId());

        String name = request.getMuscleGroups().stream()
                .map(Enum::name).collect(Collectors.joining("_"));
        session.setName(name);

        List<Exercise> exercises = exerciseRepository.findByMuscleGroupsIn(new ArrayList<>(request.getMuscleGroups()));
        for (Exercise ex : exercises) {
            Exercise copy = new Exercise(ex.getName(), ex.getSetsJson(), ex.getMuscleGroups());
            copy.setSession(session);
            session.getExercises().add(copy);
        }

        return sessionRepository.save(session);

    }

    @DeleteMapping("/{id}")
    public void deleteSession(@PathVariable Long id) {
        System.out.println("Delete session: " + id);
        sessionRepository.deleteById(id);

    }

}
