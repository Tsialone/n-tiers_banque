package com.example.dto;

import java.io.Serializable;

public class DeviseDto  implements Serializable{
    private Long id;
    private String libelle;
    private String dateDebut; // String au lieu de LocalDate
    private String dateFin;   // String au lieu de LocalDate
    private double arriary;

    public DeviseDto() {}

    public DeviseDto(Long id, String libelle, String dateDebut, String dateFin, double arriary) {
        this.id = id;
        this.libelle = libelle;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.arriary = arriary;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public String getDateDebut() { return dateDebut; }
    public void setDateDebut(String dateDebut) { this.dateDebut = dateDebut; }

    public String getDateFin() { return dateFin; }
    public void setDateFin(String dateFin) { this.dateFin = dateFin; }

    public double getArriary() { return arriary; }
    public void setArriary(double arriary) { this.arriary = arriary; }
}
