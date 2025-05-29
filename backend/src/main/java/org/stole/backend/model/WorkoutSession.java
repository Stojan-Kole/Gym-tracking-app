package org.stole.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class WorkoutSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String notes;

    public WorkoutSession(LocalDate date, String notes) {
        this.date = date;
        this.notes = notes;
    }


    public WorkoutSession() {

    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }


    public String getNotes() {
        return notes;
    }

    public LocalDate getDate() {
        return date;
    }
}
