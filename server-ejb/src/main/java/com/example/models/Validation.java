package com.example.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "validations")
public class Validation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_validation")
    private Integer idValidation;

    @ManyToOne
    @JoinColumn(name = "id_transaction", nullable = false)
    private TransactionCourant transaction;

    @Column(length = 12, nullable = false)
    private String etat = "en_attente"; 
    @Column(name = "date_validation", nullable = false)
    private LocalDate dateValidation = LocalDate.now();
}
