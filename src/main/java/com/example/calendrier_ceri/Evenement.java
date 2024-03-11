package com.example.calendrier_ceri;

import java.time.LocalDateTime;

public class Evenement {
    private String description;
    private String location;
    private LocalDateTime debut;
    private LocalDateTime fin;

    public Evenement(String description, String location, LocalDateTime debut, LocalDateTime fin) {
        this.description = description;
        this.location = location;
        this.debut = debut;
        this.fin = fin;
    }

    // Getters
    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getDebut() {
        return debut;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    // Setters
    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDebut(LocalDateTime debut) {
        this.debut = debut;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", debut=" + debut +
                ", fin=" + fin +
                '}';
    }
}
