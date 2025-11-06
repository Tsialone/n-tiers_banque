package com.example.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "validations_transaction")
public class ValidationTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_validation")
    private Integer idValidation;

    @ManyToOne
    @JoinColumn(name = "id_transaction", nullable = false)
    private TransactionCourant transaction;

    @ManyToOne
    @JoinColumn(name = "id_etat", nullable = false)
    private Etat etat;

    @Column(name = "date_validation", nullable = false)
    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDateTime dateValidation = LocalDateTime.now();
}
