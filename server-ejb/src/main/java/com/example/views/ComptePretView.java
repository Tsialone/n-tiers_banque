package com.example.views;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ComptePretView {
    private int idCompte;
    private int idClient;
    private String libelle;
    private LocalDate dateOuverture;
    private BigDecimal capitalEmprunte;
    private BigDecimal tauxInteret;
    private int dureeMois;
    private LocalDate dateEcheance;
    private String statut;
    private BigDecimal solde = BigDecimal.ZERO;
}
