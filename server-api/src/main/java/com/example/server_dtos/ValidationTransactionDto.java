package com.example.server_dtos;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ValidationTransactionDto implements Serializable {
    private Integer idValidation;
    private Integer idTransaction;
    private Integer idEtat;
    private LocalDateTime dateValidation;

    private EtatDto etat;
}
