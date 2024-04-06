package com.example.calendrier_ceri;

import java.time.ZonedDateTime;

public class CalendarActivity {
    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;
    private String description;
    private String location;

    private String summary;

    private String enseignant;
    private String matiere;

    private String salle;

    private boolean isExam;


    public boolean isExam() {
        return isExam;
    }

    public CalendarActivity(ZonedDateTime startDateTime, String summary, String enseignant, String matiere, ZonedDateTime endDateTime, String description, String location , String salle, boolean isExam) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.description = description;
        this.location = location;
        this.summary = summary;
        this.enseignant= enseignant;
        this.matiere= matiere;
        this.salle=salle;
        this.isExam=isExam;
    }
    public CalendarActivity(ZonedDateTime zonedStartDateTime, ZonedDateTime zonedEndDateTime,String description, String loc){
        this.startDateTime = zonedStartDateTime;
        this.endDateTime = zonedEndDateTime;
        this.description = description;
        this.location = loc;
    }

    // Getters and Setters

    public ZonedDateTime getStartDateTime() {
        return startDateTime;
    }
    public String getSummary() {
        return summary;
    }

    public String getSalle() {
        return salle;
    }

    public void setSalle(String salle) {
        this.salle = salle;
    }

    public void setStartDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public ZonedDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(ZonedDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
    public String getEnseignant() {

        return enseignant;
    }


    public String getEmailEnseignant(){
        return getEnseignant()+"@univ-avignon.fr";
    }




    @Override
    public String toString() {
        return "CalendarActivity{" +
                "startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
