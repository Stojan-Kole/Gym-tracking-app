package org.stole.backend.controller;

import org.springframework.web.bind.annotation.*;

import org.stole.backend.model.WorkoutSession;
import org.stole.backend.repository.WorkoutSessionRepository;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/sessions")
public class WorkoutSessionController {

    private final WorkoutSessionRepository repository;

    public WorkoutSessionController(WorkoutSessionRepository repository) {
        this.repository = repository;

    }

    @GetMapping
    public List<WorkoutSession> getAllSessions() {
        System.out.println("Get all sessions");
        return repository.findAll();

    }

    @PostMapping
    public WorkoutSession createSession(@RequestBody WorkoutSession session) {
        System.out.println("Received: " + session);
        for (WorkoutSession s: repository.findAll()) {
            System.out.println(s.getDate() + " " + s.getNotes());
        }
        return repository.save(session);
    }

    @DeleteMapping("/{id}")
    public void deleteSession(@PathVariable Long id) {
        repository.deleteById(id);

    }

}
