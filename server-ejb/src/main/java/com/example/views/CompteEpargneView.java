package com.example.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.example.dto.TransactionEpargneDto;

import lombok.Data;

@Data
public class CompteEpargneView implements Serializable {

    private int idCompte;
    private int idClient;

    private BigDecimal capitalEpargne;
    private String libelle;
    private LocalDate dateOuverture;
    private BigDecimal tauxInteret;
    private BigDecimal retrait;
    private BigDecimal solde = BigDecimal.ZERO;

    private List<TransactionEpargneDto> transactions;
}
