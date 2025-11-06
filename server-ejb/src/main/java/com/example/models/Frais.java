package com.example.models;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "frais")
public class Frais implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_frais")
    private Integer idFrais;

    @ManyToOne
    @JoinColumn(name = "id_type_compte", nullable = false)
    private TypeCompte typeCompte;

    @Column(name = "montant_inf", nullable = false)
    private Double montantInf = 0.0;

    @Column(name = "montant_sup", nullable = true)
    private Double montantSup;

    @Column(name = "fond_montant", nullable = false)
    private Double fondMontant = 0.0;

    @Column(name = "fond_pourcentage", nullable = false)
    private Double fondPourcentage = 0.0;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    public Double getFraisValue(Double montant) throws Exception {
        double montantFinal = 0.0;

        Double montantInf = this.getMontantInf();
        Double montantSup = this.getMontantSup();
        Double fondPourcentage = this.getFondPourcentage();
        Double fondMontant = this.getFondMontant();

        if (montantSup == null)
            montantSup = Double.MAX_VALUE;

        if (montant >= montantInf && montant <= montantSup) {
            montantFinal = (montant * (fondPourcentage / 100.0)) + fondMontant;
        }

        return montantFinal;
    }
}
