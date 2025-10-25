package com.example.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;

@Data
public class TransactionCourantDto  implements Serializable{

    private Integer idTransaction;
    private Integer idCompte;        
    private LocalDate dateTransaction = LocalDate.now();
    private String libelle;
    private Double montant;
    private String sens;
    private boolean validate;
    private String devise;
}
