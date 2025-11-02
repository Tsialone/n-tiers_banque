package com.example.server_dtos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class TransactionCourantDto implements Serializable {

    private Integer idTransaction;
    private Integer idCompte;
    private Integer idCompteDest;
    private LocalDate dateTransaction = LocalDate.now();
    private String libelle;
    private Double montant;
    private String sens;
    private boolean validate;
    private String devise;

    private List<ValidationDto> validations;
    private ValidationDto lastValidation;
}
