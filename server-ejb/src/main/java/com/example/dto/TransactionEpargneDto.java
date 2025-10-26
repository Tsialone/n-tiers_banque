package com.example.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class TransactionEpargneDto implements Serializable {

    private int idTransaction;
    private int idCompte;
    private LocalDate dateTransaction;
    private String libelle;
    private BigDecimal montant;
    private String sens; // "debit" ou "credit"
}
