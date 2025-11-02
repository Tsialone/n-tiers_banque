package com.example.server_dtos;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ComptePretDto {
    private int idClient;
    private LocalDate dateOuverture = LocalDate.now();
    private String libelle;
    private BigDecimal capitalEmprunte;
    private BigDecimal tauxInteret;
    private int dureeMois;
    private LocalDate dateEcheance; // nullable
    private String statut = "actif";
}
