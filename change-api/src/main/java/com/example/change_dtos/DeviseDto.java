package com.example.change_dtos;

import java.io.Serializable;
import java.time.LocalDate;

public class DeviseDto implements Serializable {
    private Long id;
    private String libelle;
    private String dateDebut; // String au lieu de LocalDate
    private String dateFin; // String au lieu de LocalDate
    private double arriary;

    private Boolean valide;
    private String dateValidation;

    public DeviseDto() {
    }

    public DeviseDto(Long id, String libelle, String dateDebut, String dateFin, double arriary) {
        this.id = id;
        this.libelle = libelle;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.arriary = arriary;
    }
    public String getDateValidation() {
        return dateValidation;
    }
    public void setDateValidation(String dateValidation) {
        this.dateValidation = dateValidation;
    }
     public String getEtat() {
        if (valide == null) return "En attente";
        return valide ? "Validé" : "Annulé";
    }
    public Boolean isValide() {
        return valide;
    }
    public void setValide(Boolean valide) throws Exception {
        if (this.valide != null) {
            throw new Exception("Vous avez deja valider:" + this.valide);
        }
        this.valide = valide;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) throws Exception {
        if (libelle.isEmpty() || libelle == null)
            throw new Exception("Libelle ne doit pas etre null");
        this.libelle = libelle;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) throws Exception {
        if (dateDebut == null || dateDebut.isEmpty())
            throw new Exception("La date du devise ne doit pas etre null");
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) throws Exception {
        if (dateFin != null && !dateFin.isEmpty()) {
            LocalDate finDate = LocalDate.parse(dateFin);
            if (finDate.isBefore(getDateDebutDate())) {
                throw new Exception(
                        "Date debut: " + getDateDebutDate() + " doit etre apres date fin: " + getDateFinDate());
            }
        }
        this.dateFin = dateFin;
    }

    public double getArriary() {
        return arriary;
    }

    public void setArriary(double arriary) throws Exception {
        if (arriary <= 0)
            throw new Exception("Le montant en ar ne doit pas etre null ou 0");
        this.arriary = arriary;
    }

    public LocalDate getDateFinDate() {
        if (dateFin == null || dateFin.isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateFin);
    }

    public LocalDate getDateDebutDate() {
        if (dateDebut == null || dateDebut.isEmpty()) {
            System.out.println("itooooooooooooooo e " + dateDebut);
            return null;
        }
        return LocalDate.parse(dateDebut);
    }

    // @Override
    // public boolean equals(Object o) {
    // if (this == o)
    // return true;
    // if (!(o instanceof DeviseDto))
    // return false;
    // DeviseDto d = (DeviseDto) o;
    // return Objects.equals(libelle, d.libelle); // uniquement libelle
    // }

    // @Override
    // public int hashCode() {
    // return Objects.hash(libelle); // uniquement libelle
    // }
}
