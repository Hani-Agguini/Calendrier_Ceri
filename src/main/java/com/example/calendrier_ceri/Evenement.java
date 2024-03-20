package com.example.calendrier_ceri;

import java.time.LocalDateTime;

public class Evenement {
    private String description;
    private String summary;

    private String enseignant;
    private String matiere;
    private String location;
    private LocalDateTime debut;
    private LocalDateTime fin;

    public Evenement(String description,String summary,String enseignant,String matiere, String location, LocalDateTime debut, LocalDateTime fin) {
        this.description = description;
        this.summary = summary;
        this.enseignant= enseignant;
        this.matiere= matiere;
        this.location = location;
        this.debut = debut;
        this.fin = fin;
    }

    // Getters
    public String getDescription() {
        return description;
    }

    public String getSummary() {
        return summary;
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
    public String getMatiere() {
        return matiere;
    }
    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public void setEnseignant(String enseignant) {
        this.enseignant = enseignant;
    }
    public void getEnseignant(String enseignant) {
        this.enseignant = enseignant;
    }
    @Override
    public String toString() {
        return "Evenement{" +
                "description='" + description + '\'' +
                ", summary='" + summary + '\'' +
                ", enseignant='" + enseignant + '\'' +
                ", matiere='" + matiere + '\'' +
                ", location='" + location + '\'' +
                ", debut=" + debut +
                ", fin=" + fin +
                '}';
    }

    public String getEnseignant() {
        return enseignant;
    }
}
