package com.example.server_dtos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class TransactionCourantDto implements Serializable {
    private Integer idTransaction;
    private String source;
    private LocalDate dateTransaction;
    private String libelle;
    private Double montant;
    private String sens;
    private String devise;


    private List<ValidationTransactionDto> validations;
    private ValidationTransactionDto lastValidation;
}
