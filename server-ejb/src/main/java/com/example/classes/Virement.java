package com.example.classes;

import java.time.LocalDate;
import com.example.models.CompteCourant;

public class Virement {

    private CompteCourant compteDebit;
    private CompteCourant compteCredit;
    private LocalDate dateVirement;
    private String devise = "MG";
    private Double montant;

    public Virement (){
        
    }
    public Virement(CompteCourant compteDebit, CompteCourant compteCredit,
            LocalDate dateVirement, Double montant, String devise) throws Exception {

        setCompteDebit(compteDebit);
        setCompteCredit(compteCredit);
        setDateVirement(dateVirement);
        setMontant(montant);
        setDevise(devise);
    }

    public void setCompteDebit(CompteCourant compteDebit) throws Exception {
        if (compteDebit == null) {
            throw new Exception("Le compte débiteur ne doit pas être null.");
        }
        this.compteDebit = compteDebit;
    }

    public void setCompteCredit(CompteCourant compteCredit) throws Exception {
        if (compteCredit == null) {
            throw new Exception("Le compte créditeur ne doit pas être null.");
        }
        this.compteCredit = compteCredit;
    }

    public void setDateVirement(LocalDate dateVirement) throws Exception {
        if (dateVirement == null) {
            throw new Exception("La date du virement ne peut pas être null.");
        }
        if (dateVirement.isAfter(LocalDate.now())) {
            throw new Exception("La date du virement ne peut pas être dans le futur.");
        }
        if (dateVirement.isBefore(LocalDate.now())) {
            throw new Exception("La date du virement ne peut pas être dans le passé.");
        }
        this.dateVirement = dateVirement;
    }

    public void setMontant(Double montant) throws Exception {
        if (montant == null || montant <= 0) {
            throw new Exception("Le montant doit être supérieur à zéro.");
        }
        this.montant = montant;
    }

    public void setDevise(String devise) throws Exception {
        if (devise == null || devise.isBlank()) {
            throw new Exception("La devise ne peut pas être vide.");
        }
        this.devise = devise;
    }

    public CompteCourant getCompteCredit() {
        return compteCredit;
    }

    public CompteCourant getCompteDebit() {
        return compteDebit;
    }

    public LocalDate getDateVirement() {
        return dateVirement;
    }

    public Double getMontant() {
        return montant;
    }

    public String getDevise() {
        return devise;
    }
}
