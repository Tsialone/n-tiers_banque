package com.example.models;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@Entity
@Table(name = "virements")
public class Virement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_virement")
    private Integer idVirement;

    @Column(name = "id_object", nullable = false, unique = true)
    private String idObject; // ex: "vrmt_1"

    @ManyToOne
    @JoinColumn(name = "id_compte_deb", nullable = false)
    private CompteCourant compteDebit;

    @ManyToOne
    @JoinColumn(name = "id_compte_cred", nullable = false)
    private CompteCourant compteCredit;

    @Column(name = "date_virement", nullable = false)
    private LocalDateTime dateVirement = LocalDateTime.now();

    @Column(nullable = false)
    private Double montant;

    @Column(name = "taux", nullable = false)
    private Double taux;

    @Column(nullable = false)
    private String devise = "ar";

    @OneToMany(mappedBy = "virement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ValidationVirement> validations;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "source", referencedColumnName = "id_object", insertable = false, updatable = false)
    private List<TransactionCourant> transactions;

    @ManyToOne
    @JoinColumn(name = "id_frais")
    private Frais frais;

    @Column(name = "frais_montant", nullable = false)
    private Double faraisMontant = 0.0;

    @OneToMany(mappedBy = "virement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HistoriqueVirement> historiques = new ArrayList<>();

    @Transient
    public HistoriqueVirement getLastHistorique() {
        if (historiques == null || historiques.isEmpty()) {
            return null;
        }
        return historiques.stream()
                .max(Comparator.comparing(HistoriqueVirement::getDateAction))
                .orElse(null);
    }

    public boolean isValide (){
        ValidationVirement lastValidate = getLastValidation();
        if (lastValidate == null) return false;
        if (lastValidate.getEtat() == null) return false;
        if (lastValidate.getEtat().getLibelle().equals("valider")) return true;
        return false;
    }
    // public void setFrais(List<Frais> frais) throws Exception {
    // if (frais == null)
    // throw new Exception("Le frais ne doit pas etre null");
    // this.frais = frais;
    // }

    // public Frais getFraisAssoc() throws Exception {
    // if (frais == null || frais.isEmpty()) {
    // throw new Exception("Aucun frais disponible pour ce type de compte");
    // }

    // double montant = getMontant();
    // // double montantFinal = montant;

    // for (Frais f : frais) {
    // Double montantInf = f.getMontantInf();
    // Double montantSup = f.getMontantSup();
    // // Double fondPourcentage = f.getFondPourcentage();
    // // Double fondMontant = f.getFondMontant();

    // if (montantSup == null)
    // montantSup = Double.MAX_VALUE;

    // if (montant >= montantInf && montant <= montantSup) {
    // return f;
    // }
    // }

    // return null;
    // }

    // public Double getFraisValue() throws Exception {
    // Frais f = getFraisAssoc();
    // if (f == null) {
    // throw new Exception("Aucun frais disponible pour ce type de compte");
    // }
    // double montant = getMontant();
    // double montantFinal = montant;

    // Double montantInf = f.getMontantInf();
    // Double montantSup = f.getMontantSup();
    // Double fondPourcentage = f.getFondPourcentage();
    // Double fondMontant = f.getFondMontant();

    // if (montantSup == null)
    // montantSup = Double.MAX_VALUE;

    // if (montant >= montantInf && montant <= montantSup) {
    // montantFinal = (montant * (fondPourcentage / 100.0)) + fondMontant;
    // }

    // return montantFinal;
    // }

    public void setFrais(Frais frais) throws Exception {

        if (frais == null)
            throw new Exception("Le frais ne doit pas etre null");

        this.frais = frais;
    }

    public Frais findFrais(List<Frais> listeFrais) throws Exception {
        if (listeFrais == null || listeFrais.isEmpty()) {
            throw new Exception("Aucun frais disponible pour ce type de compte");
        }

        double montant = getMontant();
        // double montantFinal = montant;

        for (Frais f : listeFrais) {
            Double montantInf = f.getMontantInf();
            Double montantSup = f.getMontantSup();
            // Double fondPourcentage = f.getFondPourcentage();
            // Double fondMontant = f.getFondMontant();

            if (montantSup == null)
                montantSup = Double.MAX_VALUE;

            if (montant >= montantInf && montant <= montantSup) {
                return f;
            }
        }

        return null;
    }

    public Double getMontantWithFrais() throws Exception {
        Frais f = getFrais();
        if (f == null) {
            throw new Exception("Aucun frais disponible pour ce type de compte");
        }
        double montant = getMontant();
        double montantFinal = montant;

        Double montantInf = f.getMontantInf();
        Double montantSup = f.getMontantSup();
        Double fondPourcentage = f.getFondPourcentage();
        Double fondMontant = f.getFondMontant();

        if (montantSup == null)
            montantSup = Double.MAX_VALUE;

        if (montant >= montantInf && montant <= montantSup) {
            montantFinal = montant + (montant * (fondPourcentage / 100.0)) + fondMontant;
        }

        return montantFinal;
    }

    public HistoriqueVirement generateHistorique(Etat etat, ClientCourant utilisateur, String aciton) {
        HistoriqueVirement hist = new HistoriqueVirement();
        hist.setVirement(this);
        hist.setIdCompteDebit(this.getCompteDebit().getIdCompte());
        hist.setIdCompteCredit(this.getCompteCredit().getIdCompte());
        hist.setMontant(this.getMontant());
        hist.setDevise(this.getDevise());
        hist.setDateVirement(this.getDateVirement());
        hist.setAction(aciton);
        hist.setEtat(etat);
        hist.setTaux(this.getTaux());
        hist.setDateAction(LocalDateTime.now());

        if (utilisateur == null)
            hist.setUtilisateur(compteDebit.getClient());
        hist.setUtilisateur(utilisateur);

        return hist;
    }

    public ValidationVirement changeEtat(Etat etat, ClientCourant utilisateur, boolean doubleChangeControl)
            throws Exception {

        if (etat == null)
            throw new Exception("L'etat ne doit pas etre null");
        if (utilisateur == null)
            throw new Exception("Le validateur ne doit pas etre null");
        ValidationVirement validation = new ValidationVirement();
        validation.setVirement(this);
        validation.setDateValidation(LocalDateTime.now());
        validation.setEtat(etat);
        validation.setUtilisateur(utilisateur);
        ValidationVirement last = getLastValidation();
        Etat currentEtat = null;
        if (last != null)
            currentEtat = last.getEtat();
        if (doubleChangeControl
                &&
                currentEtat != null
                &&
                !validations.isEmpty()
                && !currentEtat.getLibelle().equals("en_attente"))
            throw new Exception("Etat déja " + getLastValidation().getEtat().getLibelle());

        return validation;
    }

    public void setCompteDebit(CompteCourant compteDebit) throws Exception {
        if (compteDebit == null)
            throw new Exception("Le compte debiteur ne du ne doit pas etre null ou inexistant");
        this.compteDebit = compteDebit;
    }

    public void setCompteCredit(CompteCourant compteCredit) throws Exception {
        if (compteCredit == null)
            throw new Exception("Le compte crediteur ne du ne doit pas etre null ou inexistant");
        this.compteCredit = compteCredit;
    }

    public void setMontant(double montant) throws Exception {
        if (montant <= 0)
            throw new Exception("La montant du ne doit pas etre <= 0");
        this.montant = montant;
    }

    public void setDateVirement(LocalDateTime dateVirement) throws Exception {
        if (dateVirement == null)
            throw new Exception("La date ne doit pas etre null");
        if (!dateVirement.toLocalDate().equals(LocalDate.now()))
            throw new Exception("La date du virement doit etre aujourdhui");
        this.dateVirement = dateVirement;
    }

    @Transient
    public ValidationVirement getLastValidation() {
        if (validations == null || validations.isEmpty()) {
            return null;
        }
        return validations.stream()
                .max(Comparator.comparing(ValidationVirement::getDateValidation))
                .orElse(null);
    }
}
