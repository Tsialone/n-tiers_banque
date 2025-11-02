package com.example.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import com.example.change_dtos.DeviseDto;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "transactions_courant")
public class TransactionCourant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaction")
    private Integer idTransaction;

    @ManyToOne
    @JoinColumn(name = "id_compte", nullable = false)
    @JsonbTransient
    private CompteCourant compte;

    @Column(name = "id_virement", nullable = true)
    private Integer idVirement;

    @ManyToOne
    @JoinColumn(name = "id_compte_dest", nullable = true)
    @JsonbTransient
    private CompteCourant compteDest;

    @Column(name = "date_transaction")
    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDate dateTransaction;

    private String libelle;

    private Double montant;

    @Column(name = "devise", nullable = false)
    private String devise = "MG";

    @Column(name = "validate")
    private boolean validate = false;

    @Column(length = 6)
    private String sens;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Validation> validations;

    @JsonbTransient
    public Validation getLastValidation() {
        if (validations == null || validations.isEmpty()) {
            return null;
        }
        return validations.stream()
                .max(Comparator.comparing(Validation::getDateValidation))
                .orElse(null);
    }

    public boolean updateByDevise(DeviseDto deviseDto) {
        // Vérifie si la devise correspond (ex: "USD" == "USD")
        if (!getDevise().equalsIgnoreCase(deviseDto.getLibelle())) {
            return false;
        }

        LocalDate dateTransaction = getDateTransaction();
        LocalDate dateDebut = LocalDate.parse(deviseDto.getDateDebut());
        LocalDate dateFin = null;

        if (deviseDto.getDateFin() != null && !deviseDto.getDateFin().isEmpty()) {
            dateFin = LocalDate.parse(deviseDto.getDateFin());
        }

        boolean dansPeriode;
        if (dateFin != null) {
            dansPeriode = !dateTransaction.isBefore(dateDebut) && !dateTransaction.isAfter(dateFin);
        } else {
            dansPeriode = !dateTransaction.isBefore(dateDebut);
        }

        if (dansPeriode) {
            setMontant(deviseDto.getArriary());
            return true;
        }

        return false;
    }

}
