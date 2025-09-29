package com.example.models;

import java.time.LocalDate;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "transactions_courant")
public class TransactionCourant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaction")
    private Integer idTransaction;

    @ManyToOne
    @JoinColumn(name = "id_compte", nullable = false)
    @JsonbTransient
    private CompteCourant compte;

    @Column(name = "date_transaction")
    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDate dateTransaction;

    private String libelle;

    private Double montant;

    @Column(length = 6)
    private String sens;

}
