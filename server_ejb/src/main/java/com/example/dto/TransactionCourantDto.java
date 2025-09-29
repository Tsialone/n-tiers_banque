package com.example.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TransactionCourantDto {

    private Integer idTransaction;
    private Integer idCompte;        
    private LocalDate dateTransaction;
    private String libelle;
    private Double montant;
    private String sens;
}
