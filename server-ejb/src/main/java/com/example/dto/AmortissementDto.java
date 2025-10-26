package com.example.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AmortissementDto {
    private int idAmortissement;
    private int idCompte;
    private int mois;
    private BigDecimal mensualite;
    private BigDecimal interet;
    private BigDecimal capital;
    private BigDecimal resteDu;
    private LocalDate createdAt;
    private String statut;
    private int compteIdClient;
}
