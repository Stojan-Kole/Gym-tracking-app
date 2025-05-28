package org.stole.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stole.backend.model.WorkoutSession;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {
}
