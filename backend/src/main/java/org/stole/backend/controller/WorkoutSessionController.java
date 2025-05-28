package org.stole.backend.controller;

import org.springframework.web.bind.annotation.*;

import org.stole.backend.model.WorkoutSession;
import org.stole.backend.repository.WorkoutSessionRepository;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "http://localhost:5173")
public class WorkoutSessionController {

    private final WorkoutSessionRepository repository;

    public WorkoutSessionController(WorkoutSessionRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<WorkoutSession> getAllSessions() {
        return repository.findAll();
    }

    @PostMapping
    public WorkoutSession getSession(@RequestBody WorkoutSession session) {
        return repository.save(session);
    }

    @DeleteMapping("/{id}")
    public void deleteSession(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
