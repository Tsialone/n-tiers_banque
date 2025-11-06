package com.example.models;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.change_dtos.DeviseDto;

@Entity
@Data
@Table(name = "comptes_courant")
public class CompteCourant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compte")
    private Integer idCompte;

    private String nom;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    @JsonbTransient
    private ClientCourant client;

    @ManyToOne
    @JoinColumn(name = "id_type_compte", nullable = false)
    private TypeCompte typeCompte;

    @Column(name = "date_ouverture")
    private LocalDate dateOuverture = LocalDate.now();

    @Column(name = "capital")
    private Double capital;

    @Column(name = "plafond", nullable = false)
    private Double plafond;

    @Column(name = "id_object", nullable = false, unique = true)
    private String idObject;

    @Column(name = "decouvert_autorise")
    private Double decouvertAutorise = 0.0;

    @OneToMany(mappedBy = "compteCredit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Depot> depots;

    @OneToMany(mappedBy = "compteDebit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Retrait> retraits;

    @OneToMany(mappedBy = "compteDebit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonbTransient
    private List<Virement> virementsEmis = new ArrayList<>();

    @OneToMany(mappedBy = "compteCredit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonbTransient
    private List<Virement> virementsRecus = new ArrayList<>();

    // @OneToMany(mappedBy = "compte", cascade = CascadeType.ALL, fetch =
    // FetchType.LAZY)
    // private List<TransactionCourant> transactions;

    public TransactionCourant makeTransactionByVirement(Virement virement) throws Exception {
        if (virement == null)
            throw new Exception("Le virement ne doit pas etre null");
        TransactionCourant transactionCourant = new TransactionCourant();
        transactionCourant.setSource(virement.getIdObject());
        transactionCourant.setDateTransaction(LocalDate.now());
        transactionCourant.setDevise(virement.getDevise());
        transactionCourant.setMontant(virement.getMontant());
        double fraisDeduire = virement.getFrais().getFraisValue(virement.getMontant());
        if (this.getIdCompte().equals(virement.getCompteDebit().getIdCompte())) {
            // transactionCourant.setMontant(virement.getMontantWithFrais());
            transactionCourant.setMontant(virement.getMontant() );

            // transactionCourant.setMontant(virement.getMontant());
            // controlVirement(virement.getMontant());
            transactionCourant.setLibelle("Virement à " + virement.getCompteCredit().getIdObject());
            transactionCourant.setSens("debit");
        } else if (!this.getIdCompte().equals(virement.getCompteCredit().getIdCompte())) {

            // transactionCourant.setMontant(virement.getMontant() - fraisDeduire);
            transactionCourant.setMontant(virement.getMontant() - fraisDeduire);

            transactionCourant.setLibelle("Virement de " + virement.getCompteDebit().getIdObject());
            transactionCourant.setSens("credit");
        } else {
            // virement.setMontant(fraisDeduire);
            transactionCourant.setBanque(true);
            transactionCourant.setMontant(fraisDeduire);
            transactionCourant.setLibelle("Frais du virement de " + virement.getCompteDebit().getIdObject() + " à "
                    + virement.getCompteCredit().getIdObject());
            transactionCourant.setSens("credit");
        }

        return transactionCourant;
    }

    public void controlRetrait(Double montant) throws Exception {
        if (montant == null || montant <= 0) {
            throw new Exception("Le montant du retrait doit être supérieur à 0");
        }
        
        // if (getSolde() == 0) {
        //     throw new Exception("Le solde d");
        // }

        if (montant > this.getSolde()) {
            throw new Exception("Solde insuffisant pour effectuer le retrait. Solde actuel : " + this.getSolde() + " vs " + montant);
        }
    }

    public TransactionCourant makeTransactionByRetrait(Retrait retrait) throws Exception {
        if (retrait == null)
            throw new Exception("Le retrait ne doit pas être null");

        // Vérification du solde ou des règles métier
        controlRetrait(retrait.getMontant());

        TransactionCourant transactionCourant = new TransactionCourant();
        transactionCourant.setSource(retrait.getIdObject());
        transactionCourant.setDateTransaction(LocalDate.now());
        transactionCourant.setDevise(retrait.getDevise()); // ou retrait.getDevise() si tu as la devise
        transactionCourant.setMontant(retrait.getMontant());

        if (this.getIdCompte().equals(retrait.getCompteDebit().getIdCompte())) {
            transactionCourant.setLibelle("Retrait depuis le compte " + retrait.getCompteDebit().getIdObject());
            transactionCourant.setSens("debit");
        }

        // Si tu veux gérer des frais ou des transactions bancaires pour retrait, tu
        // peux les ajouter ici
        // else {
        // transactionCourant.setBanque(true);
        // transactionCourant.setLibelle("Frais du retrait " + retrait.getIdObject());
        // transactionCourant.setSens("credit");
        // }

        return transactionCourant;
    }

    public TransactionCourant makeTransactionByDepot(Depot depot) throws Exception {
        if (depot == null)
            throw new Exception("Le dépôt ne doit pas être null");

        controlDepot(depot.getMontant());
        TransactionCourant transactionCourant = new TransactionCourant();
        transactionCourant.setSource(depot.getIdObject());
        transactionCourant.setDateTransaction(LocalDate.now());
        transactionCourant.setDevise(depot.getDevise());
        transactionCourant.setMontant(depot.getMontant());

        if (this.getIdCompte().equals(depot.getCompteCredit().getIdCompte())) {
            transactionCourant.setLibelle("Dépôt sur compte " + depot.getCompteCredit().getIdObject());
            transactionCourant.setSens("credit");
        }

        // else {
        // transactionCourant.setBanque(true);
        // transactionCourant.setLibelle("Frais du dépôt " + depot.getIdObject());
        // transactionCourant.setSens("credit");
        // }

        return transactionCourant;
    }

    public double getSolde() throws Exception {
        double totalDepots = 0.0;
        double totalRetraits = 0.0;
        double totalVirementEmis = 0.0;
        double totalVirementRecus = 0.0;

        for (Depot depot : depots) {
            if (depot.isValide())
                totalDepots += depot.getMontant();
        }
        for (Retrait retrait : retraits) {
            if (retrait.isValide())
                totalRetraits += retrait.getMontant();
        }

        for (Virement virementEmis : virementsEmis) {
            if (virementEmis.isValide())
                totalVirementEmis += virementEmis.getMontant();
        }

        for (Virement virementRecus : virementsRecus) {
            if (virementRecus.isValide()) {
                double frais = 0.0;
                if (virementRecus.getFrais() != null) {
                    frais = virementRecus.getFrais().getFraisValue(virementRecus.getMontant());
                }
                totalVirementRecus += virementRecus.getMontant() - frais;
            }
        }

        return totalDepots + totalVirementRecus - totalRetraits - totalVirementEmis + getCapital();

    }

    public Double getTotalVirement() {
        Double sum = 0.0;
        for (Virement virement : virementsEmis) {
            if (virement.isValide())
                sum += virement.getMontant();
        }
        return sum;
    }

    public void controlVirement(double montant) throws Exception {
        Double totalVirement = getTotalVirement() + montant;
        Double solde = getSolde();
        if (totalVirement > this.getPlafond())
            throw new Exception(
                    "Vous avez atteint le plafond maximal de ce compte " + this.getIdObject() + " " + totalVirement
                            + " ar vs " + getPlafond());
        if (solde < montant)
            throw new Exception("Solde insuffisant pour ce compte " + solde + " ar vs " + montant);
    }

    public void controlDepot(double montant) throws Exception {
        // Double totalVirement = getTotalVirement() + montant;
        Double solde = getSolde();
        // if (totalVirement > this.getPlafond())
        // throw new Exception(
        // "Vous avez atteint le plafond maximal de ce compte " + this.getIdObject() + "
        // " + totalVirement
        // + " ar vs " + getPlafond());
        // if (solde < montant)
            // throw new Exception("Solde insuffisant pour ce compte " + solde + " ar vs " + montant);
    }

    public Virement virer(CompteCourant compteCredit, Double taux, List<Frais> frais, LocalDateTime dateVirement,
            Double montant,
            String devise)
            throws Exception {
        controlVirement(montant);

        Virement virement = new Virement();
        virement.setIdObject("");
        virement.setCompteDebit(this);
        virement.setCompteCredit(compteCredit);
        virement.setDateVirement(dateVirement);
        virement.setDevise(devise);
        virement.setMontant(montant);
        virement.setTaux(taux);
        virement.setFrais(virement.findFrais(frais));
        // virement.setFaraisMontant(virement.getFraisValue());

        if (this.getIdCompte().equals(compteCredit.getIdCompte())) {
            throw new Exception("Vous ne pouvez pas faire un virement à vous-même; débiteur: "
                    + this.getIdCompte() + " créditeur: " + compteCredit.getIdCompte());
        }

        return virement;
    }
}
