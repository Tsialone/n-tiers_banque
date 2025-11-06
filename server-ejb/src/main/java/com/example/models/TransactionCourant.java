package com.example.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import com.example.repositories.DepotRepository;
import com.example.repositories.RetraitRepository;
import com.example.repositories.VirementRepository;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import lombok.Data;
import lombok.val;

@Data
@Entity
@Table(name = "transactions_courant")
public class TransactionCourant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaction")
    private Integer idTransaction;

    @Column(nullable = false)
    private String source; // ex: "vrm_1"

    @Column(name = "date_transaction", nullable = false)
    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDate dateTransaction = LocalDate.now();

    @Column(nullable = false, length = 100)
    private String libelle;

    @Column(nullable = false)
    private Double montant;

    @Column(length = 6, nullable = false)
    private String sens; // debit / credit

    @Column(nullable = false)
    private String devise = "MG";

    @Column(nullable = false)
    private boolean banque = false;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ValidationTransaction> validations;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "source", referencedColumnName = "id_object", insertable =
    // false, updatable = false)
    // private Virement virement;

    // @Column(name = "source", nullable = false)
    // private String source;

    @Transient
    public Virement getVirement(VirementRepository virementRepository) {
        return virementRepository.findBySource(source);
    }

    @Transient
    public Depot getDepot(DepotRepository depotRepository) {
        return depotRepository.findBySource(source);
    }
    
    @Transient
    public Retrait getRetrait(RetraitRepository retraitRepository) {
        return retraitRepository.findBySource(source);
    }

    public void setDevise(String devise) throws Exception {
        if (devise == null || devise == null)
            throw new Exception("La devise ne doit pas etre null");
        this.devise = devise;
    }

    public ValidationTransaction changeEtat(Etat etat, boolean doubleChangeControl) throws Exception {

        if (etat == null)
            throw new Exception("L'etat ne doit pas etre null");
        ValidationTransaction validation = new ValidationTransaction();
        validation.setTransaction(this);
        validation.setDateValidation(LocalDateTime.now());
        validation.setEtat(etat);
        ValidationTransaction last = getLastValidation();
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

    @JsonbTransient
    public ValidationTransaction getLastValidation() {
        if (validations == null || validations.isEmpty())
            return null;
        return validations.stream()
                .max(Comparator.comparing(ValidationTransaction::getDateValidation))
                .orElse(null);
    }
}
